#import <Cocoa/Cocoa.h>
#import <AppKit/AppKit.h>
#import "GuiController.h"
#import "AppDelegate.h"


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
@implementation GuiController
- (id) initWithDelegate:(AppDelegate *) theDelegate
{
    // set self to result of initializing parent. If initialization succeeds
    if ( (self = [super init]))
    {
        // set property and increment the reference count for its object.
        updateTimer = 0;
        appDelegate = [theDelegate retain];
        song = [[[NSString alloc] init] autorelease];
        songList = [[[NSMutableArray alloc] init] autorelease];
        readAndSave = YES;
    }
    return self;
}

- (void) dealloc
{
    //decrease the reference count for all instance variable objects.
    [appDelegate release];
    [updateTimer release];
    [sound release];
    [song release];
    [songList release];
    [sc release];
    [super dealloc];
}

// methods to handle button clicks
- (void) openWav
{
    [self debug:[NSString stringWithFormat:@"openWav called:\n"]];
    NSOpenPanel *panel;
    NSURL *path;

    panel = [NSOpenPanel openPanel];
    [panel setAllowsMultipleSelection:NO];
    NSArray   *fileTypes = [NSArray arrayWithObjects:@"wav", @"mp3", nil];
    //[panel setAllowedFileTypes: [NSSound soundUnfilteredTypes]];
    [panel setAllowedFileTypes:fileTypes]; //panel.setAllowedFileTypes(fileTypes);
    [panel runModal];
    [self debug:[NSString stringWithFormat:@"found %d urls\n",
                 ((int)[[panel URLs] count])]];
    path = [[panel URLs] objectAtIndex:0];
    // turn debug to yes to see results of URL methods absoluteString
    // lastPathComponent, and path.
    // These are written to the file MusicPlayer.app/DebugMessageLog.txt
    NSURL *anURL = ((NSURL *)[[panel URLs] objectAtIndex:0]);
    NSString *pathStr = [anURL absoluteString];
    [self debug:[NSString stringWithFormat:@"Music absolute url string: %s\n",
                 [pathStr UTF8String]]];
    [self debug:[NSString stringWithFormat:@"Music last path component: %s\n",
                 [[path lastPathComponent] UTF8String]]];
    [self debug:[NSString stringWithFormat:@"Music path component: %s\n",
                 [[path path] UTF8String]]];
    if (sound != nil)
    {
        [sound stop];
        [sound release];
    }
    sound = [[NSSound alloc] initWithContentsOfURL:path byReference:NO];
    if (!sound)
    {
        NSRunAlertPanel (@"Error",
                         @"Could not open selected file", @"OK", nil, nil);
        return;
    }

    [sound setDelegate:self];
    [[appDelegate slider] setMaxValue:[sound duration]];
    [[NSApp keyWindow]
     setTitle:[NSString stringWithFormat:@"Cst420 Music Player: %s",
               [[path lastPathComponent] UTF8String]]];

    if (!updateTimer)
    {
        updateTimer = [NSTimer scheduledTimerWithTimeInterval:0.2
                       target:self
                       selector:@selector(update)
                       userInfo:nil
                       repeats:YES];
    }
}

- (void) playWav
{
    [self debug:[NSString stringWithFormat:@"playWav called: "]];
    [self debug:[NSString stringWithFormat:@"Play = %d\n", [sound play]]];
}

- (void) stopWav
{
    [self debug:[NSString stringWithFormat:@"stopWav called: "]];
    [self debug:[NSString stringWithFormat:@"Stop = %d\n", [sound stop]]];
}

- (void) pauseWav
{
    [self debug:[NSString stringWithFormat:@"pauseWav called: "]];
    [self debug:[NSString stringWithFormat:@"Pause = %d\n", [sound pause]]];
}

- (void) resumeWav
{
    [self debug:[NSString stringWithFormat:@"resumeWav called: "]];
    [self debug:[NSString stringWithFormat:@"Resume = %d\n", [sound resume]]];
}

- (void) endIt
{
    [self debug:[NSString stringWithFormat:@"Exit button pushed: %s\n",
                 [@"Completing program." UTF8String]]];
    [NSApp terminate:self];
}

// newTime gets called when user changes the slider value since I'm a
// slider target and action
- (void) newTime:(id)sender
{
    [self debug:[NSString stringWithFormat:@"New Time = %g\n",
                 [sender doubleValue]]];
    [sound setCurrentTime:[sender doubleValue]];
}

//sound gets called when sound finishes playing
//since I'm a sound delegate
- (void)sound:(NSSound *)aSound didFinishPlaying:(BOOL)finishedPlaying
{
    if (finishedPlaying == NO && 0 )
    {
        NSRunAlertPanel (@"Error", @"Playback ended with errors",
                         @"OK", nil, nil);
        return;
    }
}

- (void) update
{
    //[self debug:[NSString stringWithFormat:@"Update Called\n"]];
    [[appDelegate timeLab] setStringValue:
     [NSString stringWithFormat:@"%d:%02d / %d:%02d",
      (int)[sound currentTime] / 60,
      (int)[sound currentTime] % 60,
      (int)[sound duration] / 60,
      (int)[sound duration] % 60]];
    [[appDelegate slider] setDoubleValue:[sound currentTime]];
}

- (NSMutableArray *) getSongList
{
    sc = [[[CoreSockets alloc] initWithHost:@"localhost" port:@"3030"] autorelease];
    [sc sendAnInt:2];
    song = [sc receiveAString];

    while(![song isEqual:@"end"]) 
    {
      if(song != nil) {
        readAndSave = YES;
        if(readAndSave) 
        {
          [songList addObject:song];
          readAndSave = NO;
        } 
      }
      song = [sc receiveAString];
    }
    NSLog(@"%@", songList);
    return songList;
}

- (void) debug:(NSString *) aMessage
{
    //if (DEBUGON)
    //{
        /**
         *    To write to current directory use the file manager to find cwd
         *    NSFileManager *filemgr;
         *    NSString *currentPath;
         *    filemgr = [[NSFileManager alloc] init];
         *    currentPath = [filemgr currentDirectoryPath];
         *    NSString * path = [currentPath stringByAppendingString:
         *                                                     @"DebugMessageLog.txt"];
         **/
        //NSBundle*aBundle = [NSBundle bundleForClaas:[self class]];
        NSBundle *aBundle = [NSBundle mainBundle];
        // path to application: .../MusicPlayer/MusicPlayer.app
        NSString *pathSeg = [aBundle bundlePath];
        NSString *fileName = [NSString stringWithFormat:@"%s/%s",
                              [pathSeg UTF8String], [@"DebugMessageLog.txt" UTF8String]];
        NSFileHandle *fh = [NSFileHandle fileHandleForWritingAtPath:fileName];
        [fh seekToEndOfFile];
        [fh writeData:[aMessage dataUsingEncoding:NSUTF8StringEncoding]];
        [fh closeFile];
        //printf("debug: %s\n", [aMessage UTF8String]);
    //}
}

@end
