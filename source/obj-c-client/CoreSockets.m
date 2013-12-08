#import "CoreSockets.h"

#define DEBUGON YES

/**
 * Purpose: Objective-C/Cocoa class for client socket connections using
 * the core foundation classes.
 * Cst420 Foundations of Distributed Applications
 * see http://pooh.poly.asu.edu/Cst420
 * @author Tim Lindquist (Tim.Lindquist@asu.edu), ASU Polytechnic, Engineering
 * @version December 2013
 * @author Christian Murphy
 */
@implementation CoreSockets

@synthesize inStream;
@synthesize outStream;

-(id) initWithHost:(NSString *) host port:(NSString *)portNum
{
    if ((self = [super init]))
    {
        aHost = [host retain];
        aPort = [portNum retain];

        NSInputStream *readStream;
        NSOutputStream *writeStream;
        [NSStream getStreamsToHost:[NSHost hostWithName:aHost]
         port:[aPort integerValue]
         inputStream:&readStream
         outputStream:&writeStream];
        [self setInStream:(NSInputStream *)readStream];
        [self setOutStream:(NSOutputStream *)writeStream];
        //inStream = (NSInputStream *)readStream;
        //outStream = (NSOutputStream *)writeStream;
        [readStream release];
        [writeStream release];

        [inStream setDelegate:self];
        [outStream setDelegate:self];
        [inStream open];
        [outStream open];
        [inStream scheduleInRunLoop:[NSRunLoop currentRunLoop]
         forMode:NSDefaultRunLoopMode];
        [outStream scheduleInRunLoop:[NSRunLoop currentRunLoop]
         forMode:NSDefaultRunLoopMode];
    }
    return self;
}

- (void) sendTheString:(NSString *)aString
{
    NSString *toSend = [NSString stringWithFormat:@"songtoclient^%s",
                        [aString UTF8String]];
    NSData *data = [[NSData alloc]
                    initWithData:[aString dataUsingEncoding:NSUTF8StringEncoding]];
    [outStream write:[data bytes] maxLength:[data length]];
    [self debug:[NSString stringWithFormat:@"Sent %lu bytes to %s:%s\n",
                 (unsigned long)[data length], [aHost UTF8String], [aPort UTF8String]]];
}

- (void) sendAnInt:(NSInteger)intToSend
{
   NSData* data = [[[NSData alloc] initWithBytes:&intToSend length:sizeof(intToSend)] 
                        autorelease];
   printf("data is %@", data);
   [outStream write: [data bytes] maxLength:[data length]];
   [self debug:[NSString stringWithFormat:@"Sent %lu bytes to %s:%s\n",
            (unsigned long)[data length], [aHost UTF8String], [aPort UTF8String]]];
}

- (NSString *) receiveAString
{
    NSString *aStr = nil;
    uint8_t buffer[4096];
    int n = [inStream read:buffer maxLength:sizeof(buffer)];
    if (n > 0)
    {
        [self debug:[NSString stringWithFormat:@"in receive, got: %d bytes\n", n]];
        NSData *outDat = [[[NSData alloc] initWithBytes:buffer length:n]
                          autorelease];
        aStr = [[[NSString alloc] initWithData:outDat
                 encoding:NSUTF8StringEncoding] autorelease];
    }
    else
    {
        [self debug:@"read didn't find any bytes.\n"];
    }
    return aStr;
}

// - (NSInteger) receiveAnInt
// {
//     NSString *aInt = nil;
//     NSInteger newInt;
//     uint8_t buffer[4096];
//     int n = [inStream read:buffer maxLength:sizeof(buffer)];
//     if(n > 0)
//     {
//         [self debug:[NSString stringWithFormat:@"in receive, got: %d bytes\n", n]];
//         NSData *receiveData = [[[NSData alloc] initWithBytes:buffer length:n] autorelease];
//         aInt = [[[NSString alloc] initWithData:receiveData encoding:NSUTF8StringEncoding] autorelease];
//         newInt = [aInt intValue];
//     }

//     return newInt;
// }

- (void) closeConnection
{
    [inStream close];
    [outStream close];
}

- (void) stream:(NSStream *)theStream handleEvent:(NSStreamEvent)streamEvent
{
    [self debug:[NSString stringWithFormat:@"stream event %lu\n", streamEvent]];
    switch (streamEvent)
    {
    case NSStreamEventOpenCompleted:
        [self debug:@"stream opened\n"];
        break;
    case NSStreamEventHasBytesAvailable:
        [self debug:@"stream bytes available\n"];
        if (theStream == inStream)
        {
            uint8_t buf[4096];
            int len;
            while ([inStream hasBytesAvailable])
            {
                len = [inStream read:buf maxLength:sizeof(buf)];
                if (len > 0)
                {
                    [self debug:[NSString stringWithFormat:@"read %d bytes\n", len]];
                    //NSString * found = [[NSString alloc] initWithBytes:buf
                    //                   length:len encoding:NSASCIIStringEncoding];
                }
            }
        }
        break;
    case NSStreamEventErrorOccurred:
        [self debug:@"stream error occurred\n"];
        break;
    case NSStreamEventEndEncountered:
        [self debug:@"stream end encountered; closing\n"];
        [theStream close];
        [theStream removeFromRunLoop:[NSRunLoop currentRunLoop]
         forMode:NSDefaultRunLoopMode];
        break;
    default:
        [self debug:@"unknown stream event\n"];
    }
}

- (void)dealloc
{
    // don’t forget to release allocated objects!
    [inStream release];
    [outStream release];
    [aHost release];
    [aPort release];
    [super dealloc];
}

- (void) debug:(NSString *) aMessage
{
    if (DEBUGON)
    {
        //NSBundle*aBundle = [NSBundle bundleForClaas:[self class]];
        NSBundle *aBundle = [NSBundle mainBundle];
        // path to application: .../SampleNiblessApp/NiblessApp.app
        NSString *pathSeg = [aBundle bundlePath];
        NSString *fileName = [NSString stringWithFormat:@"%s/%s",
                              [pathSeg UTF8String], [@"DebugMessageLog.txt" UTF8String]];
        NSFileHandle *fh = [NSFileHandle fileHandleForWritingAtPath:fileName];
        [fh seekToEndOfFile];
        [fh writeData:[aMessage dataUsingEncoding:NSUTF8StringEncoding]];
        [fh closeFile];
    }
}

@end
