package service;

import interfaces.Verifiable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import model.Resource;
import model.ResourceRequest;

public class HandoverService implements Verifiable
{
    private RequestService requestService;
    private ResourceService resourceService;

    public HandoverService(RequestService requestService, ResourceService resourceService)
    {
        this.requestService = requestService;
        this.resourceService = resourceService;
    }

    public String generateCode(String requestId, String ownerId)
    {
        ResourceRequest req = requestService.findRequest(requestId);
        if (req == null)
        {
            return null;
        }
        Resource r = resourceService.findById(req.getResourceId());
        if (r == null)
        {
            return null;
        }
        if (!r.getOwnerId().equals(ownerId))
        {
            return null;
        }
        if (!ResourceRequest.STATUS_ACCEPTED.equals(req.getStatus()))
        {
            return null;
        }
        String code = makeRandomCode();
        req.setHandoverCodeHash(scramble(requestId + ":" + code));
        return code;
    }

    public boolean verify(String requestId, String borrowerId, String code)
    {
        ResourceRequest req = requestService.findRequest(requestId);
        if (req == null)
        {
            return false;
        }
        if (!req.getRequesterId().equals(borrowerId))
        {
            return false;
        }
        if (!ResourceRequest.STATUS_ACCEPTED.equals(req.getStatus()))
        {
            return false;
        }
        if (req.getHandoverCodeHash() == null)
        {
            return false;
        }
        if (!req.getHandoverCodeHash().equals(scramble(requestId + ":" + code)))
        {
            return false;
        }
        Resource r = resourceService.findById(req.getResourceId());
        if (r == null)
        {
            return false;
        }
        if (!r.borrowResource(borrowerId))
        {
            return false;
        }
        req.setHandoverCodeHash(null);
        return requestService.getTracker().updateStatus(req, ResourceRequest.STATUS_COMPLETED);
    }

    private String makeRandomCode()
    {
        Random dice = new Random();
        int n = dice.nextInt(10000);
        return String.format("%04d", n);
    }

    private String scramble(String text)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
            {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }
}