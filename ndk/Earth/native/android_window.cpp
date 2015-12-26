#include "gl/window.hpp"

namespace ui {

class android_layer : public window_layer
{
public:
	using parameters = window_layer::parameters;
	
	android_layer() {}  //!< \note okno vytvara java cast kodu, takze parametre nemaju vplyv na okno
	~android_layer() {}
	
	struct user_input
	{};
};

}  // ui
