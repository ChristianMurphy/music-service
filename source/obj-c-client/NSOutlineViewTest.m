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
 * @version December 2013
 * @author Christian Murphy
 */

#import "NSOutlineViewTest.h"


@implementation NSOutlineViewTest: NSObject

-(id) initInWindow:(NSWindow *)theWindow with:(NSMutableArray *)theSongs
{
    NSTableColumn *keyColumn;
    NSScrollView *scrollView;
    songList = [[[NSMutableArray alloc] init] autorelease];
    song = [[[NSString alloc] init] autorelease];
    [songList addObjectsFromArray:theSongs];
    NSLog(@"%@", theSongs);
    window = theWindow;
    [window retain];
    keyColumn = [[NSTableColumn alloc] initWithIdentifier:@"Albums and Songs"];
    [keyColumn autorelease];
    [keyColumn setEditable:NO];
    [[keyColumn headerCell] setStringValue:@"Albums and Songs"];
    [keyColumn setMinWidth:100];

    outlineView = [[NSOutlineView alloc]
                   initWithFrame:NSMakeRect (0, 0, 520, 170)];
    [outlineView retain];
    [outlineView addTableColumn:keyColumn];
    [outlineView setOutlineTableColumn:keyColumn];
    [outlineView setDrawsGrid:NO];
    [outlineView setIndentationPerLevel:10];
    [outlineView setAllowsColumnReordering:NO];
    [outlineView setAutoresizesOutlineColumn:YES];
    [outlineView setIndentationMarkerFollowsCell:YES];

    [outlineView setDataSource:self];
    [outlineView setDelegate:self];

    [outlineView registerForDraggedTypes:
     [NSArray arrayWithObject:NSStringPboardType]];


    scrollView = [[NSScrollView alloc]
                  initWithFrame:NSMakeRect (20, 100, 520, 170)];
    [scrollView setDocumentView:outlineView];
    [outlineView release];
    [scrollView setHasHorizontalScroller:YES];
    [scrollView setHasVerticalScroller:YES];
    [scrollView setBorderType:NSBezelBorder];
    [scrollView setAutoresizingMask:(NSViewWidthSizable
                                     | NSViewHeightSizable)];

    [outlineView sizeToFit];

    [[window contentView] addSubview:scrollView];
    [scrollView release];
    return self;
}

// required methods for data source
// Names of children for each node
- (id)outlineView:(NSOutlineView *)outlineView
    child:(NSInteger)index
    ofItem:(id)item
{

    [self debug:[NSString stringWithFormat:@"child: %"PRIiPTR" ofItem: %@\n",
                 index, item]];
    //This is going to have all the songs put into it
    if ([item isEqual:@"Music Library"])
    {
        song = [songList objectAtIndex:index];
        return song;
    }
    else if (item == nil && index == 0)
    {
        return @"Music Library";
    }

    return nil;
}

//Nodes are capable of expanding
- (BOOL)outlineView:(NSOutlineView *)outlineView
    isItemExpandable:(id)item
{
    [self debug:[NSString stringWithFormat:@"In isItemExpandable: %@\n", item]];

    if ([item isEqual:@"Music Library"])
    {
        return YES;
    }
    return NO;
}

//Return the number of children each parent has
- (NSInteger) outlineView:(NSOutlineView *)outlineView
    numberOfChildrenOfItem:(id)item
{
    [self debug:[NSString stringWithFormat:@"In numberOfChildren: %@\n", item]];
    if (item == nil)
    {
        return 1;
    }
    else if ([item isEqual:@"Music Library"])
    {
        //return array.length
        NSLog(@"%@", songList);
        return [songList count];
    }
    return 0;
}

//DA BIG CHEESE. This shit actually files the songs in a tree
- (id) outlineView:(NSOutlineView *)outlineView
    objectValueForTableColumn:(NSTableColumn *)tableColumn
    byItem:(id)item
{
    NSString *value = nil;
    //[self debug: [NSString stringWithFormat:@"value for tab col: item = %@\n",
    //                     item]];
    if ([item isEqual:@"Music Library"])
    {
        if ([[[tableColumn headerCell] stringValue] isEqual:@"Albums and Songs"])
        {
            value = @"Music Library";
        }
    }

    if ([[[tableColumn headerCell] stringValue] isEqual:@"Music Library"])
    {
        value = item;
    }

    return value;
}

// delegate methods

- (void) outlineView:(NSOutlineView *)aTableView
    willDisplayCell:(id)aCell
    forTableColumn:(NSTableColumn *)aTableColumn
    item:(id)item
{
    //[self debug:@"outlineView:willDisplayCell:forTableColumn:item:\n"];
}

- (BOOL) outlineView:(NSOutlineView *)anOutlineView
    shouldSelectItem:(id)item
{
    [self debug:[NSString stringWithFormat:@"should select item %@ ?\n", item]];
    return YES;
}

- (BOOL) outlineView:(NSTableView *)aTableView
    writeItems:(NSArray *) items
    toPasteboard:(NSPasteboard *) pboard
{
    [self debug:[NSString stringWithFormat:@"writeItems %@\n", items]];
    [pboard declareTypes:[NSArray arrayWithObject:NSStringPboardType]
     owner:self];

    [pboard setPropertyList:@"1"
     forType:NSStringPboardType];
    return YES;
}

- (NSDragOperation) outlineView:(NSOutlineView *) ov
    validateDrop:(id <NSDraggingInfo>) info
    proposedItem:(id) item
    proposedChildIndex:(NSInteger) childIndex
{
    [self debug:[NSString stringWithFormat:@"Validate drop %@ at %"PRIiPTR"",
                 item, childIndex]];
    return NSDragOperationCopy;
}

- (void) debug:(NSString *) aMessage
{
    if (DEBUGON)
    {
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
    }
}

@end
