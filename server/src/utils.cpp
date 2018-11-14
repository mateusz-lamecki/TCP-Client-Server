#include "utils.h"

std::vector<std::string> split_string(std::string x, char delimiter) {
    std::vector<std::string> words;

    std::string tmp;
    for(char c: x) {
        if(c == delimiter) {
            words.push_back(tmp);
            tmp.clear();
        } else {
            tmp += c;
        }
    }

    if(!tmp.empty()) words.push_back(tmp);
    return words;
}
