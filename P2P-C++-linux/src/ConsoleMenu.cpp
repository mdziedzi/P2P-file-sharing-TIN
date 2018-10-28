#include "ConsoleMenu.h"
#include "Controller.h"
#include "Node.h"
#include <iostream>
#include <vector>

using namespace std;

ConsoleMenu::ConsoleMenu() {}

ConsoleMenu::~ConsoleMenu()
{
    //dtor
}

void ConsoleMenu::run(void){
    Controller controller;
    while(true){
        cout << "1 - Zapytaj o wezly w sieci." << endl;
        cout << "exit - wyjscie." << endl;
        string line;
        getline(cin, line);
        system("clear");
        if (line == "1"){
            vector <Node> nodes = controller.get_active_nodes();
            cout << "Dostepne wezly w sieci: " << endl;
            for(vector<Node>::iterator it = nodes.begin(); it != nodes.end(); ++it) {
                cout << it->get_internet_address() << endl;
            }
        }
        if (line == "exit"){
            return;
        }

    }
}


