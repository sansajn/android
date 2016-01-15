#include "resource.hpp"

path_manager & path_manager::ref()
{
	static path_manager pman;
	return pman;
}

void path_manager::root_path(std::string const & p)
{
	_root_path = p;
}

std::string path_manager::translate_path(std::string const & p)
{
	return _root_path + "/" + p;
}
