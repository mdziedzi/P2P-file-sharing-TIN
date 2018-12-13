package tin.p2p.controller;

public interface ControllerGUIInterface {

    /**
     * Declares methods that will be performed after attempt to create new net.
     */
    interface CreateNewNetCallback {

        /**
         * Method is called after the net was created successfully.
         */
        Void onCreateNewNetSuccess();

        /**
         * Method is called after something went wrong with creation of new net.
         */
        Void onCreateNewNetFailure();

    }

    /**
     * Declares methods that will be performed after attempt to connectting to the net by specific IP.
     */
    interface ConnectToNetByIPCallback {

        /**
         * Method is called after user connected to net via specific IP number ended with success.
         */
        Void onConnectToNetByIPSucces();

        /**
         * Method is called after host with specific IP number reject user request.
         */
        Void onConnectToNetByIPReject();

        /**
         * Method is called after failure when connecting to specific net.
         */
        Void onConnectToNetByIPFailure();

    }

    /**
     * Declares method that will be performed after attempt to disconnect from the net.
     */
    interface DisconnectCallback {

        /**
         * Method called after disconnect from the net will be performed successfully.
         */
        Void onDisconnectSuccess();

        /**
         * Method called after disconnect from the net will not be performed for some reasons.
         */
        Void onDisconnectFailure();

    }

}

