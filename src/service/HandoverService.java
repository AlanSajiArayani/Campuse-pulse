package service;

import java.util.Random;

import interfaces.Verifiable;

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
        return null;
    }

    public boolean verify(String requestId, String borrowerId, String code)
    {
        return false;
    }
    private String makeRandomCode()
    {
        Random dice=new Random();
        int n= dice.nextInt(10000);
        return String.format("%04d", n);
    }
}