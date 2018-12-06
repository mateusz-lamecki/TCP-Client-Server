#pragma once

#include <vector>
#include <string>


namespace sk2 {

namespace utils {

std::vector<std::string> split_string(std::string x, std::string delimiter);
std::string vector_to_str(const std::vector<std::string> messages, std::string delimiter);

} // namespace utils

} // namespace sk2
