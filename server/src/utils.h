#pragma once

#include <vector>
#include <string>


namespace sk2 {

namespace utils {

std::vector<std::string> split_string(std::string x, std::string delimiter);
std::vector<std::string> extract_params(std::string x);

} // namespace utils

} // namespace sk2
