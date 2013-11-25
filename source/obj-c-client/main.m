#import <Cocoa/Cocoa.h>
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
 * @version November 2012
 */
int main(int argc, char *argv[]) {
    // create an autorelease pool
    NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
    // make sure the application singleton has been instantiated
    NSApplication * application = [NSApplication sharedApplication];
    // instantiate the application delegate
    AppDelegate * applicationDelegate =
                          [[[AppDelegate alloc] init] autorelease];
    // assign the delegate to the NSApplication
    [application setDelegate:applicationDelegate];
    // call the run method of the application
    [application run];
    // drain the autorelease pool
    [pool drain];
    // execution never gets here ..
    return 0;
}
