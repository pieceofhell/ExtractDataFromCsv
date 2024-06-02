#include <iostream>
#include <sstream>
#include <vector>

std::vector<std::string> split(const std::string &s, char delimiter) {
    std::vector<std::string> tokens;
    std::string token;
    std::stringstream ss(s);
    
    while (std::getline(ss, token, delimiter)) {
        tokens.push_back(token);
    }
    
    return tokens;
}

int main() {
    std::string str = "one,,two,three,four";
    char delimiter = ',';
    std::vector<std::string> result = split(str, delimiter);
    
    for (const auto &s : result) {
        std::cout << s << std::endl;
    }

    return 0;
}
