#include "utils.h"

namespace sk2 {

namespace utils {

std::vector<std::string> split_string(std::string x, std::string delimiter) {
    /* Splits input into smaller strings, seperated by delimiter */
    std::vector<std::string> words;
    int del_size = (int)delimiter.size();

    std::string tmp;
    for(int i=0; i<(int)x.size(); ++i) {
        if(i >= int(del_size) && x.substr(i-del_size+1, del_size) == delimiter) {
            for(int j=0; j<del_size-1; ++j) if(!tmp.empty()) tmp.erase(--tmp.end());
            if(!tmp.empty()) words.push_back(tmp);
            tmp.clear();
        } else {
            tmp += x[i];
        }
    }

    if(!tmp.empty()) words.push_back(tmp);
    return words;
}

std::string vector_to_str(const std::vector<std::string> messages, std::string delimiter) {
    std::string result;

    bool first = true;
    for(const std::string &message: messages) {
        if(!first) result += delimiter;
        first = false;
        result += message;
    }

    return result;
}


} // namespace utils

} // namespace sk2
