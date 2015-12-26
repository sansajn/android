//! sablonovy parameter Impl je implementaciou triedy basic_window
template <typename Impl>
class window : public Impl
{
public:
	using parameters = typename Impl::parameters;

	window(parameters const & p = parameters());
	virtual ~window() {}

	void reshape(int w, int h) override;

	unsigned width() const {return _w;}
	unsigned height() const {return _h;}
	float aspect_ratio() const {return float(_w)/float(_h);}
	glm::ivec2 center() const {return glm::ivec2(_w/2, _h/2);}

	void bind_as_render_target();

private:
	unsigned _w, _h;  //!< window geometry
};  // window

class event_handler
{
public:
	enum class button  //!< mouse buttons
	{
		left,
		middle,
		right,
		wheel_up,
		wheel_down,
		number_of_buttons
	};

	enum class state  //!< button states
	{
		down,
		up
	};

	enum class wheel
	{
		up,
		down
	};

	enum modifier  //! \note can be combination of modifiers
	{
		none = 0,
		shift = 1,
		ctrl = 2,
		alt = 4
	};

	enum class key  //!< special keys
	{
		caps_lock,
		f1,
		f2,
		f3,
		f4,
		f5,
		f6,
		f7,
		f8,
		f9,
		f10,
		f11,
		f12,
		print_screen,
		scroll_lock,
		pause,
		insert,
		home,
		page_up,
		end,
		page_down,
		right,
		left,
		down,
		up,
		unknown
	};

	virtual void display() {}
	virtual void reshape(int w, int h) {}
	virtual void idle() {}
	virtual void close() {}
	virtual void mouse_click(button b, state s, modifier m, int x, int y) {}
	virtual void mouse_motion(int x, int y) {}
	virtual void mouse_passive_motion(int x, int y) {}
	virtual void mouse_wheel(wheel w, modifier m, int x, int y) {}
	virtual void key_typed(unsigned char c, modifier m, int x, int y) {}
	virtual void key_released(unsigned char c, modifier m, int x, int y) {}
	virtual void special_key(key k, modifier m, int x, int y) {}
	virtual void special_key_released(key k, modifier m, int x, int y) {}
};  // event_handler

//! zaklad pre okno
class basic_window : public event_handler
{
public:
	virtual ~basic_window() {}
	virtual void start() = 0;

	class parameters  //!< parametre okna pri jeho vytvarani
	{
	public:
		parameters();

		unsigned width() const {return _w;}
		unsigned height() const {return _h;}
		std::string const & name() const {return _name;}
		bool debug() const {return _debug;}
		std::pair<int, int> version() const {return _version;}

		parameters & size(unsigned w, unsigned h) {_w = w; _h = h; return *this;}
		parameters & geometry(unsigned w, unsigned h) {return size(w, h);}
		parameters & name(std::string const & s) {_name = s; return *this;}
		parameters & debug(bool d) {_debug = d; return *this;}
		parameters & version(int major, int minor) {_version = std::make_pair(major, minor); return *this;}

	private:
		unsigned _w, _h;
		std::string _name;
		bool _debug;
		std::pair<int, int> _version;  // (major, minor)
	};
};  // basic_window

//! okno pracujuce v pool mode (vhodne pre dynamicke sceny)
class basic_pool_window : public basic_window
{
public:
	virtual void input(float dt) {}
	virtual void update(float dt) {}
	float fps() const {return std::get<0>(_fps);}
	std::tuple<float, float, float> const & fps_stats() const {return _fps;}  //!< \return returns (current, min, max) fps triplet

private:
	// schovaj vsetky eventy okna
	void mouse_motion(int x, int y) override;
	void mouse_passive_motion(int x, int y) override;
	void mouse_click(button b, state s, modifier m, int x, int y) override;
	void mouse_wheel(wheel w, modifier m, int x, int y) override;
	void key_typed(unsigned char c, modifier m, int x, int y) override;
	void key_released(unsigned char c, modifier m, int x, int y) override;
	
	std::tuple<float, float, float> _fps;  // (current, min, max)
	bool _closed = false;
};


class android_pool_impl : public basic_window
{
public:
	void start() override {}  //!< \note androidne okno nema vlastny loop
	virtual void input(float dt) {}
	virtual void update(float dt) {}
};


void init_func();  //!< vytvorenie/re-vytvorenie kontextu
void display_func();  //!< okno potrebuje prekreslit
void reshape_func(int w, int h);  //!< zmena geometrie okna
void idle_func();  //!< okenny system nema co robyt (urene k implementacii taskou na pozadi, napr. update, ...)
void close_func();  //!< okno sa zatvara
void mouse_func(int mouse_btn, int btn_state, int x, int y);  //! stlacenie a pustenie tlacitka mysi
void motion_func(int x, int y);  //!< pohyb mysi pri stlacenom tlacitku vo vnutri okna
void passive_motion_func(int x, int y);  //!< pohyb mysi vo vnutri okna
void keyboard_func(unsigned char c, int x, int y);  //!< stlacenie klavesi generujuce ASCII znak
void keyboard_up_func(unsigned char c, int x, int y);  //!< pustenie ASCII klavesi
void special_func(int k, int x, int y);  //!< sltacenie neznakovej klavesi (fN, sipky, ...)
void special_up_func(int k, int x, int y);  //!< pustenie neznakovej klavesi


basic_window * active_window();
void create_default_window(int w, int h);
void destroy_default_window();

scene_window * __default_window = nullptr;  // v pripade androidu, nech existuje iba jedno okno

basic_window * active_window()
{
	return __default_window;
}

void destroy_default_window()
{
	delete __default_window;
	_default_window = nullptr;
}

void create_default_window(int w, int h)
{
	assert(__default_window && "default window already created");
	__default_window = new scene_window{window::parameters{}.geometry(w, h)};
}

void init_func()
{
	// prve vytvorenie egl kontextu, alebo znovu vytvorenie po strate
	if (active_window())
		destroy_default_window();
}

void reshape_func(int w, int h)
{
	if (!active_window)  // vytvor okno (ak sa tak este nestalo)
		create_default_window(w, h);
	w->reshape(w, h);
}

void display_func() 
{
	/* v androide predpokladam, ze display() sa vola kontinualne (podobne ako idle(), 
	nie len pri zmene obsahu okna), takze je mozne metodu vyuzit k update a input-e okna. */
	basic_window * w = active_window();
	assert(w && "invalid window");
	w->input(dt);
	w->update(dt);
	w->display();
}

void idle_func()
{
	active_window()->idle();
}

void close_func()
{
	active_window()->close();
	destroy_default_window();  // koniec aplykacie
}

void mouse_func(int mouse_btn, int btn_state, int x, int y)
{
	assert(0 && "not implemented");
}

void motion_func(int x, int y)
{
	assert(0 && "not implemented");
}

void passive_motion_func(int x, int y)
{
	assert(0 && "not implemented");
}

void keyboard_func(unsigned char c, int x, int y)
{
	assert(0 && "not implemented");
}

void keyboard_up_func(unsigned char c, int x, int y)
{
	assert(0 && "not implemented");
}

void special_func(int k, int x, int y)
{
	assert(0 && "not implemented");
}

void special_up_func(int k, int x, int y)
{
	assert(0 && "not implemented");
}


class NativeSceneWrapper {
	init();  // pocas zivota aplykacie moze byt volana niekolko krat
	display();
	reshape();
	keyboard();
	touch();
	close();
};

Java_init()
{
	init_func();
}

Java_reshape(int w, int h)
{
	reshape_func(w, h);
}

Java_display()
{
	display_func();
}

Java_touch()
{
	// preloz to do reci motion_func() a mouse_func()
}
