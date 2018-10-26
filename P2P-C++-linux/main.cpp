#include "server.h"
#include "client.h"
#include <unistd.h>


using namespace std;

int main(){
    Server server;
    pid_t pid = fork();
    if (pid == 0){
        server.run();
    } else if (pid > 0){
        Client client;
        client.send_message();
    }
    return 0;
}
