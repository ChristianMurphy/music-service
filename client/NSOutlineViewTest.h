#include <Foundation/Foundation.h>
#include <AppKit/AppKit.h>

// to write debug messages to a file, change NO below to YES.
// Appends messages sent to the debug: method at the end of the file
// .../MusicPlayer/MusicPlayer.app/DebugMessageLog.txt (on Mac).
// on GNUstep, you must manually copy DebugMessageLog.txt into the
// MusicPlayerGNUstep.app directory before executing with DEBUBON YES
#define DEBUGON NO

/**
 * Purpose: demonstrate music player GUI App on Cocoa without using the
 * interface builder. This program is executable on both MacOSX and on
 * Windows running GNUstep.
 * Cst420 Foundations of Distributed Applications
 * see http://pooh.poly.asu.edu/Cst420
 * @author Tim Lindquist (Tim.Lindquist@asu.edu), ASU Polytechnic, Engineering
 * based on GNUstep examples generated by Greory Casamento and Nicola.
 * with credits to Casper B Hansen for structure of nibless of apps, and
 * and Stefan Bidigaray for the use of NSSound and player app. Stefan's
 * player is released under the GNU Public License.
 * @version November 2013
 */
@interface NSOutlineViewTest: NSObject {
   NSWindow *window;
   NSOutlineView *outlineView;
}
-(id) initInWindow: (NSWindow*) window;
-(void) debug: (NSString*) aMessage;
@end