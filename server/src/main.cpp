#include <iostream>
#include "connection.h"

int main(int argc, char* argv[]) {
    sk2::Connection connection(3552);
    connection.init();

    return(0);
}
