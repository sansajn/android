#pragma once
#include "pix.hpp"

namespace pix {

//! PNG image decoder implementation (based on libpng12).
struct png_decoder : public decoder
{
	//! \note allways decode pixels as rgba8
	void decode(std::string const & fname) override;
};

}  // pix
