#pragma once
#include "platform_file_utils.h"

FileData get_asset_data(char const * relative_path);
void release_asset_data(FileData const * file_data);
