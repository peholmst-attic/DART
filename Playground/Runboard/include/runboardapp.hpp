#ifndef _RUNBOARDAPP_HPP_
#define _RUNBOARDAPP_HPP_

#include <wx/wxprec.h>

#ifndef WX_PRECOMP
    #include <wx/wx.h>
#endif

class RunboardApp: public wxApp
{
public:
    virtual bool OnInit();
};

#endif // _RUNBOARDAPP_HPP_
