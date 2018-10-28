#include "server.h"
#include "Client.h"
#include "ConsoleMenu.h"
#include <unistd.h>


using namespace std;

int main(){
    Server server;
    pid_t pid = fork();
    if (pid == 0){
        server.run();
    } else if (pid > 0){
        ConsoleMenu console_menu;
        console_menu.run();
        kill(pid, SIGKILL);
        exit(1);
    }
    return 0;
}
