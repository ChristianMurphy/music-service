#import "AppDelegate.h"
#import "GuiController.h"
#import <AppKit/NSButton.h>


/**
 * Purpose: demonstrate music player GUI App on Cocoa/GNUstep without using the
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
@implementation AppDelegate
// getters and setters to allow controller access to these controls.
@synthesize slider;
@synthesize timeLab;

- (id) init {
   if ( (self = [super init]) ) {
      // create the guicontroller so we can do the wireups for buttons as
      // the buttons are being created.
      guiController = [[GuiController alloc] initWithDelegate: self];
      // build and place the gui controls in the view
      // create a reference rect
      // set this window at position 100,150 (x,y) from lower left
      // with size 560,300 (x,y)
      NSRect contentSize = NSMakeRect(100.0f, 150.0f, 560.0f, 290.0f);
      // allocate window
      window = [[NSWindow alloc] initWithContentRect:contentSize
                                           styleMask:NSTitledWindowMask
                                             backing:NSBackingStoreBuffered
                                               defer:YES];
      [window setTitle:@"Cst420 Example Music Player"];
      // allocate view
      view = [[NSView alloc] initWithFrame:contentSize];
      [window setContentView:view];
      //songList = [[NSMutableArray alloc] init];
      //Add the buttons across the bottom of the view.
      // place the button at 20,20 (x,y) in window with width 75 and height 30
      [self addButtonWithTitle:@"Exit" //this.addButtonWithTitle(@"Exit", guiController, endIt, makeRect)
                        target:guiController
                        action:@selector(endIt)
                          rect:NSMakeRect(20,20,75,30)];
      [self addButtonWithTitle:@"Open"
                        target:guiController
                        action:@selector(openWav)
                          rect:NSMakeRect(110,20,75,30)];
      [self addButtonWithTitle:@"Play"
                        target:guiController
                        action:@selector(playWav)
                          rect:NSMakeRect(200,20,75,30)];
      [self addButtonWithTitle:@"Stop"
                        target:guiController
                        action:@selector(stopWav)
                          rect:NSMakeRect(290,20,75,30)];
      [self addButtonWithTitle:@"Pause"
                        target:guiController
                        action:@selector(pauseWav)
                          rect:NSMakeRect(380,20,75,30)];
      [self addButtonWithTitle:@"Resume"
                        target:guiController
                        action:@selector(resumeWav)
                          rect:NSMakeRect(470,20,75,30)];

      slider = [[NSSlider alloc] initWithFrame: NSMakeRect(20,60,400,20)];
      [[window contentView] addSubview: slider];
      [slider setTarget: guiController];
      [slider setAction: @selector(newTime:)];

      timeLab = [self addLabelWithTitle:@"  0 : 0 "
                                     at:NSMakeRect(445,60,80,20)];
      [timeLab retain];
      [songList addObjectsFromArray:[guiController getSongList]];
      outlineview = [[[NSOutlineViewTest alloc] initInWindow: window with:songList] retain];
      [self debug:[NSString stringWithFormat:@"AppDelegate init complete\n"]];

   }
   return self;
}

- (NSButton*) addButtonWithTitle: (NSString*)aTitle
                     target: (id) anObject
                     action: (SEL) anAction
                       rect: (NSRect) aRect {
      NSButton *button = [[[NSButton alloc] initWithFrame:aRect ] autorelease];
      [button setTitle:aTitle];
      [button setAction:anAction];
      [button setTarget: anObject];
      [button setButtonType:NSMomentaryPushButton];
      [button setBezelStyle:NSTexturedSquareBezelStyle];
      [[window contentView] addSubview: button];
      return button;
}

- (NSTextField*) addLabelWithTitle:(NSString*) aTitle at: (NSRect) aRect {
   NSTextField* label =[[[NSTextField alloc] initWithFrame: aRect] autorelease];
   [label setSelectable: YES];
   [label setBezeled: NO];
   [label setDrawsBackground: NO];
   [label setStringValue: aTitle];
   [[window contentView] addSubview: label];
   return label;
}

- (void) applicationWillFinishLaunching:(NSNotification *)notification {
   // attach the view to the window
   [window setContentView:view];
   [self debug:[NSString stringWithFormat:@"App will finish launch\n"]];
}

- (void) applicationDidFinishLaunching:(NSNotification *)notification {
   // note that printf's or NSLogs do not display on the console. Write to GUI.
   // [urlTB setStringValue:@"In application did finish launching."];
   // make the window visible.
   [window makeKeyAndOrderFront:self];
   [self debug:[NSString stringWithFormat:@"App did finish launch\n"]];
}

- (BOOL) applicationShouldTerminate:(id) sender {
   return YES;
}

- (void) debug: (NSString*) aMessage{
   if(DEBUGON){
      //NSBundle*aBundle = [NSBundle bundleForClaas:[self class]];
      NSBundle*aBundle = [NSBundle mainBundle];
      // path to application: .../MusicPlayer/MusicPlayer.app
      NSString*pathSeg = [aBundle bundlePath];
      NSString * fileName = [NSString stringWithFormat:@"%s/%s",
                     [pathSeg UTF8String],[@"DebugMessageLog.txt" UTF8String]];
      NSFileHandle* fh = [NSFileHandle fileHandleForWritingAtPath: fileName];
      [fh seekToEndOfFile];
      [fh writeData: [aMessage dataUsingEncoding:NSUTF8StringEncoding]];
      [fh closeFile];
       //printf("debug: %s\n", [aMessage UTF8String]);
   }
}

- (void)dealloc {
   // don’t forget to release allocated objects!
   [guiController release];
   [slider release];
   [timeLab release];
   [view release];
   [window release];
   [super dealloc];
}

@end
