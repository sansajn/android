#include "androidgl/android_window.hpp"
#include <stdexcept>

class scene_window : public ui::android_window
{
public:
	using base = ui::android_window;
	scene_window(parameters const & params) : base{params} {}
};

scene_window * w = nullptr;

void create(int width, int height)
{
	w = new scene_window{scene_window::parameters{}.geometry(width, height)};
	throw std::logic_error{"umelo vyvolana vinimka"};
}

void destroy()
{
	delete w;
}
