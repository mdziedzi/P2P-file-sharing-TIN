#include "console_menu.h"
#include "Controller.h"
#include "Node.h"
#include <iostream>
#include <vector>

using namespace std;

ConsoleMenu::ConsoleMenu() {}

void ConsoleMenu::run(void){
    Controller controller;
    while(true){
        cout << "1 - Zapytaj o wezly w sieci." << endl;
        cout << "exit - wyjscie." << endl;
        string line;
        getline(cin, line);
        if (line == "1"){
            cout << "Zapytanie o wezly w sieci." << endl;
            vector <Node> nodes = controller.get_active_nodes();
            for(vector<Node>::iterator it = nodes.begin(); it != nodes.end(); ++it) {
                cout << it->get_internet_address() << endl;
            }
        }
        if (line == "exit"){
            exit(1);
        }

    }
}
