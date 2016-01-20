#pragma once
//#include "al/audio.hpp"
#include <string>

namespace al {

void init_sound_system();
void free_sound_system();

struct device  // TODO: dummy
{
	void play_music(std::string const & fname) {}
	void play_effect(std::string const & fname) {}
};

extern device * default_device;

}  // al
