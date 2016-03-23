#include "runboardapp.hpp"
#include "mainframe.hpp"

wxIMPLEMENT_APP(RunboardApp);

bool RunboardApp::OnInit()
{
    MainFrame *frame = new MainFrame("Hello World", wxPoint(50, 50), wxSize(450, 340));
    frame->Show(true);
    return true;
}
