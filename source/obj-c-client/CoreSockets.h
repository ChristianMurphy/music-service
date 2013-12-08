#import <Foundation/Foundation.h>
//#import <CoreFoundation/CoreFoundation.h>

/**
 * Purpose: Objective-C/Cocoa class for client sockets using corefoundations.
 * Cst420 Foundations of Distributed Applications
 * see http://pooh.poly.asu.edu/Cst420
 * @author Tim Lindquist (Tim.Lindquist@asu.edu), ASU Polytechnic, Engineering
 * @version December 2013
 * @author Christian Murphy
 */
@interface CoreSockets : NSObject {
//    CFReadStreamRef readStream;
//    CFWriteStreamRef writeStream;
    NSInputStream * inStream;
    NSOutputStream * outStream;
    NSString * aHost;
    NSString * aPort;

}

@property (readwrite, retain, nonatomic) NSInputStream * inStream;
@property (readwrite, retain, nonatomic) NSOutputStream * outStream;

- (id) initWithHost:(NSString*) host port:(NSString*)portNum;
- (void) sendTheString:(NSString*)aString;
- (void) sendAnInt:(NSInteger)intToSend;
- (NSString*) receiveAString;
//- (NSInteger) receiveAnInt;
- (void) stream:(NSStream*)theStream handleEvent:(NSStreamEvent)streamEvent;
- (void) closeConnection;
- (void) dealloc;
- (void) debug:(NSString*)aString;
@end
