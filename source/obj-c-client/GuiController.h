#import <Cocoa/Cocoa.h>
#import <AppKit/AppKit.h>
#import "CoreSockets.h"

// to write debug messages to a file, change NO below to YES.
// Appends messages sent to the debug: method at the end of the file
// .../MusicPlayer/MusicPlayer.app/DebugMessageLog.txt (on Mac).
// on GNUstep, you must manually copy DebugMessageLog.txt into the
// MusicPlayerGNUstep.app directory before executing with DEBUGON YES
#define DEBUGON YES

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
 * @version December 2013
 * @author Christian Murphy
 */
@class AppDelegate;
// Don't implement the protocol for GNUstep where the delegate is different. 
// And, leaving it off allows default implementations of the methods
// we don't implement here.
//@interface GuiController : NSObject <NSSoundDelegate> {
@interface GuiController : NSObject {
   AppDelegate * appDelegate;
   NSSound *sound;
   NSTimer *updateTimer;
   NSMutableArray *songList;
   NSString *song;
   CoreSockets *sc;
   BOOL readAndSave;
}
- (id) initWithDelegate: (AppDelegate*) theDelegate;
- (void) dealloc;	// Other methods to be added here.

// methods to handle button clicks
- (void) endIt;
- (void) openWav;
- (void) playWav;
- (void) stopWav;
- (void) pauseWav;
- (void) resumeWav;
- (NSMutableArray *) getSongList;

// methods to manage the slider and the the NSSound object
- (void) newTime:(id)sender;
- (void) sound:(NSSound *) aSound didFinishPlaying: (BOOL) finishedPlaying;
- (void) update;

- (void) debug: (NSString*) aMessage;
@end
