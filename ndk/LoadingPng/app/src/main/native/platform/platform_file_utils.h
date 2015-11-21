#pragma once

typedef struct {
	long const data_length;
	void const * data;
	void const * file_handle;
} FileData;

FileData get_file_data(char const * path);
void release_file_data(FileData const * file_data);
