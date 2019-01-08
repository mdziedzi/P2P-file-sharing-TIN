package tin.p2p.controller_layer;

import tin.p2p.exceptions.AppPortTakenException;
import tin.p2p.exceptions.CreatingNetException;
import tin.p2p.exceptions.SavingDownloadedFileException;
import tin.p2p.exceptions.UnavailableFileToDownloadException;
import tin.p2p.layers_factory.LayersFactory;
import tin.p2p.nodes_layer.*;
import tin.p2p.utils.Properties;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class FrameworkController {
    final static Logger log = Logger.getLogger(FrameworkController.class.getName());

    private static final FrameworkController instance = new FrameworkController();
    private ControllerGUIInterface.ListOfNodesViewer listOfNodesViewer;
    private ControllerGUIInterface.ListOfFilesCallback listOfFilesCallback;
    private ControllerGUIInterface.ConnectToNetByIPCallback connectToNetByIPCallback;
    private ControllerGUIInterface.FileDownloadingCallback fileDownloadingCallback;

    private FrameworkController() {}
    private NewRemoteNodeListener newRemoteNodeListener;

    public static FrameworkController getInstance() {
        return instance;
    }
    /**
     * Creates new net based on user IP.
     */
    public void createNewNet(String password, File workspaceDirectory, ControllerGUIInterface.CreateNewNetCallback callback) {
        Properties.setWorkspaceDirectory(workspaceDirectory);

        try {
            this.newRemoteNodeListener = LayersFactory.initNewNodesListenerLayers();
        } catch (AppPortTakenException e) {
            callback.onApplicationMainPortUsed();
            return;
        } catch (CreatingNetException e) {
            callback.onCreateNewNetFailure();
            return;
        }

        try {
            PasswordRepository.setPassword(PasswordHasher.hash(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            newRemoteNodeListener.terminate();
            return;
        }
        newRemoteNodeListener.start();

        callback.onCreateNewNetSuccess();
    }

    public void registerListOfNodesViewer(ControllerGUIInterface.ListOfNodesViewer listOfNodesViewer) {
        this.listOfNodesViewer = listOfNodesViewer;
    }


    /**
     * Connect to net with specific IP.
     * @param ip IP of node we want to connect with
     * @param callback Object on which the callback will be performed.
     */
    public void connectToNetByIP(String ip, String password,
                                 File workspaceDirectory,
                                 ControllerGUIInterface.ConnectToNetByIPCallback callback) {

        Properties.setWorkspaceDirectory(workspaceDirectory);

        connectToNetByIPCallback = callback;

        try {
            password = PasswordHasher.hash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            callback.onConnectToNetByIPFailure();
            return;
        }

        PasswordRepository.setPassword(password);
        String finalPassword = password;
        CompletableFuture.supplyAsync(() -> LayersFactory.initLayersOfNewRemoteNode(ip))
                .thenApply(t -> t.connectToNetByIp(finalPassword))
                .thenAccept((r) -> connectToNetByIPCallback.onConnectToNetByIPSucces())
                .exceptionally((t) -> {
                    callback.onConnectToNetByIPFailure();
                    return null;
                });
    }

    public void getListOfFilesInNet(ControllerGUIInterface.ListOfFilesCallback callback) {
        this.listOfFilesCallback = callback;
        RemoteFileListRepository.getInstance().onNewFilesInNetRequest();

        CompletableFuture.supplyAsync(() -> {
            RemoteNodesRepository.getRemoteNodes().forEach(SenderInterface::requestForFileList);
            return null;
        });
    }

    public void getFileFromNet(String fileName, String fileHash,
                               ControllerGUIInterface.FileDownloadingCallback fileDownloadingCallback) {

        this.fileDownloadingCallback = fileDownloadingCallback;

        CompletableFuture.supplyAsync(() -> DownloadManager.getInstance().addFileDownload(fileName, fileHash))
        .exceptionally(t -> {
            if (t instanceof UnavailableFileToDownloadException) {
                this.fileDownloadingCallback.onFileNoLongerAvailable(((UnavailableFileToDownloadException) t).getFileName());
            } else if (t instanceof SavingDownloadedFileException) {
                this.fileDownloadingCallback.onSavingDownloadingFileError(((SavingDownloadedFileException) t).getFileName());
            } else {
              t.printStackTrace();
            }
            return null;
        });
    }

    public ArrayList<String> getListOfNodes() {
        return RemoteNodesRepository.getStringIpList();
    }

    public void updateViewOfRemoteNodes(ArrayList<String> remoteNodesIps) {
        if (listOfNodesViewer != null) {
            listOfNodesViewer.onListOfNodesUpdated(remoteNodesIps);
        }
    }

    public void initListeningForNewNodes() {
        if (newRemoteNodeListener == null) {
            newRemoteNodeListener = LayersFactory.initNewNodesListenerLayers();
            newRemoteNodeListener.start();
        }
    }

    public void updateViewOfFilesList(ArrayList<ArrayList<String>> listOfFiles) {
        if(listOfFiles != null)
            listOfFilesCallback.onListOfFilesReceived(listOfFiles);
    }

    public void wrongPassword() {
        if (connectToNetByIPCallback != null) {
            connectToNetByIPCallback.onConnectToNetByIPReject();
        }
    }

    public void onDownloadingFinished(String fileName) {
        if (fileDownloadingCallback != null) {
            fileDownloadingCallback.onFileDownloaded(fileName);
        }
    }

    public void endOfProgram() {
        DownloadManager.getInstance().terminate();
        RemoteNodesRepository.endOfProgram();
        if(newRemoteNodeListener != null)
            newRemoteNodeListener.terminate();
    }
}

