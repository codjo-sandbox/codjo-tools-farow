package net.codjo.tools.farow.command.teamlab.bean;

public class Result {
    private AuthentResponse response;
    private int statusCode;
    private int status;
    private int startIndex;
    private int count;


    public String getToken() {
        return response.getToken();
    }
}
