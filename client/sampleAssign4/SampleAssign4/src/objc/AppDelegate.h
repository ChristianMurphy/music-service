#import <Cocoa/Cocoa.h>
#import <APPKit/NSTextField.h>
#import "NSOutlineViewTest.h"

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
 * with credits to Casper B Hansen for structure of nibless of apps, and
 * and Stefan Bidigaray for the use of NSSound and player app. Stefan's
 * player is released under the GNU Public License.
 * @version November 2013
 */
@class GuiController;
// Don't implement the protocol for GNUstep where the delegate is different. 
// And, leaving it off allows default implementations of the methods
// we don't implement here.
//@interface AppDelegate : NSObject <NSApplicationDelegate> {
@interface AppDelegate : NSObject {
   NSWindow * window;
   NSView * view;
   //the progress slider
   NSSlider * slider;
   //the outline view (tree)
   NSOutlineViewTest * outlineview;
   //the label to the right of the slider showing play progress
   NSTextField * timeLab;
   GuiController * guiController;
}
@property (readwrite, retain, nonatomic) NSSlider * slider;
@property (readwrite, retain, nonatomic) NSTextField * timeLab;
- (id) init;
- (void) applicationWillFinishLaunching:(NSNotification *)notification;
- (void) applicationDidFinishLaunching:(NSNotification *)notification;
- (NSButton*) addButtonWithTitle: (NSString*) aTitle
                     target: (id) anObject
                     action: (SEL) anAction
                       rect: (NSRect) aRect;
- (NSTextField*) addLabelWithTitle:(NSString*) aTitle at: (NSRect) aRect;
- (BOOL) applicationShouldTerminate:(id) sender;

// a method to write debug messages to a file
- (void) debug: (NSString*) aMessage;

// dealloc cleans up the heap storage used by the object by decrementing
// reference counts (call release). dealloc should not called explicitly
// and is instead called by the runtime.
- (void) dealloc;
@end
