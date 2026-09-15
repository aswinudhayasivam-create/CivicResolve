#include "duplicate_detector.hpp"
#include <sstream>
#include <unordered_set>
#include <algorithm>
#include <cctype>
namespace civic{
static std::unordered_set<std::string> words(const std::string&s){std::unordered_set<std::string>x;std::stringstream ss(s);std::string w;while(ss>>w){std::transform(w.begin(),w.end(),w.begin(),[](unsigned char c){return std::tolower(c);});if(w.size()>1)x.insert(w);}return x;}
double similarity_score(const std::string&a,const std::string&b){auto x=words(a),y=words(b);if(x.empty()&&y.empty())return 1;if(x.empty()||y.empty())return 0;size_t i=0;for(auto&w:x)if(y.count(w))++i;return 2.0*i/(x.size()+y.size());}}