package tin.p2p.controller_layer;

import java.util.ArrayList;

public interface ControllerGUIInterface {

    /**
     * Declares methods that will be performed after attempt to create new net.
     */
    interface CreateNewNetCallback {

        /**
         * Method is called after the net was created successfully.
         */
        void onCreateNewNetSuccess();

        /**
         * Method is called after something went wrong with creation of new net.
         */
        void onCreateNewNetFailure();

        void onApplicationMainPortUsed();
    }

    /**
     * Declares methods that will be performed after attempt to connectting to the net by specific IP.
     */
    interface ConnectToNetByIPCallback {

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


        void onIPFormatFailure();
    }

    /**
     * Declares method that will be performed after attempt to disconnect from the net.
     */
    interface DisconnectCallback {

        /**
         * Method called after disconnect from the net will be performed successfully.
         */
        void onDisconnectSuccess();

        /**
         * Method called after disconnect from the net will not be performed for some reasons.
         */
        void onDisconnectFailure();

    }

    interface ListOfNodesViewer {
        void onListOfNodesUpdated(ArrayList<String> nodesIps);
    }

    interface ListOfFilesCallback {
        void onListOfFilesReceived(ArrayList<ArrayList<String>> filesList);// nazwa, hash, rozmiar, posiadacze
    }

    interface FileDownloadingCallback {
        void onFileDownloaded(String fileName);

        void onFileNoLongerAvailable(String fileName);
    }

}
