package tin.p2p.controller;

public interface ControllerGUIInterface {

    /**
     * Method is called after the net was created successfully.
     */
    void onCreateNewNetSuccess();

    /**
     * Method is called after something went wrong with creation of new net.
     */
    void onCreateNewNetFailure();

    /**
     * Method is called after user connected to net via specific IP number ended with success.
     */
    void onConnectToNetByIPSucces();

    /**
     * Method is called after host with specific IP number reject user request.
     */
    void onConnectToNetByIPReject();

    /**
     * Method is called after failure when connecting to specific net.
     */
    void onConnectToNetByIPFailure();
}

