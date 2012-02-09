package com.company.comanda.peter.server;

public interface RestaurantManagement {

    public static class WrongUserNameOrPasswordException extends Exception{

        /**
         * 
         */
        private static final long serialVersionUID = -7433356405878961437L;

        public WrongUserNameOrPasswordException() {
            super();
        }

        public WrongUserNameOrPasswordException(String message, Throwable cause) {
            super(message, cause);
        }

        public WrongUserNameOrPasswordException(String message) {
            super(message);
        }

        public WrongUserNameOrPasswordException(Throwable cause) {
            super(cause);
        }
        
    }
    long login(String username, String password) throws WrongUserNameOrPasswordException;
}
