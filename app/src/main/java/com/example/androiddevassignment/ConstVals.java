package com.example.androiddevassignment;

public class ConstVals {
    public static String serverAddress = "";
    public static String defaultServerAddress = "http://almaz3.digdes.com";
    public enum Action{
        RequestBases,
        GetBases,
        RequestToken,
        GetToken;
        public String getString(){
            switch(this){
                case RequestBases:
                    return "/SyncWebService/Api/SyncMessage/PostMessage";
                case GetBases:
                    return "/SyncWebService/Api/SyncMessage/GetMessages";
                case RequestToken:
                    return "/SyncWebService/Api/SyncMessage/PostAuthMessage";
                case GetToken:
                    return "/SyncWebService/Api/SyncMessage/GetAuthMessages";
                default:
                    return null;
            }
        }
        public String getRequestMethod(){
            switch(this){
                case RequestBases:
                case RequestToken:
                    return "POST";
                default:
                    return "GET";
            }
        }
        public String getMessageType(){
            switch(this) {
                case RequestBases:
                    return "Dv.DatabasesRequest";
                case GetBases:
                    return "Dv.DatabasesResponse";
                case RequestToken:
                    return "Dv.AuthRequest";
                case GetToken:
                    return "Dv.AuthResponse";
                default:
                    return null;
            }
        }
        public String getActionString(){
            String act = getString();
            if(act==null){
                return null;
            }
            if(act.startsWith("/")){
                act = act.substring(1);
            }
            String address = serverAddress.length()==0?defaultServerAddress:serverAddress;
            if(address.endsWith("/")){
                address = address.substring(0,address.length()-1);
            }
            return address+"/"+act;
        }
    };

}
