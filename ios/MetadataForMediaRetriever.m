#import "MetadataForMediaRetriever.h"



@implementation MetadataForMediaRetriever

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(getMetadata:(NSString *)path
resolver:(RCTPromiseResolveBlock)resolve
rejecter:(RCTPromiseRejectBlock)reject)
{
  NSFileManager *fileManager = [NSFileManager defaultManager];

  BOOL isDir;
  if (![fileManager fileExistsAtPath:path isDirectory:&isDir] || isDir){
    NSError *err = [NSError errorWithDomain:@"file not found" code:-15 userInfo:nil];
    reject([NSString stringWithFormat: @"%lu", (long)err.code], err.localizedDescription, err);
    return;
  }

  NSMutableDictionary *result = [NSMutableDictionary new];
  NSDictionary *assetOptions = @{AVURLAssetPreferPreciseDurationAndTimingKey: @YES};
  AVURLAsset *asset = [AVURLAsset URLAssetWithURL:[NSURL fileURLWithPath:path] options:assetOptions];

  NSArray *keys = [NSArray arrayWithObjects:@"commonMetadata", nil];
  [asset loadValuesAsynchronouslyForKeys:keys completionHandler:^{
    // string keys
    NSArray *items = [AVMetadataItem metadataItemsFromArray:asset.commonMetadata
    withKey:key
    keySpace:AVMetadataKeySpaceCommon];
    for (AVMetadataItem *item in items) {
      [result setObject:item.value forKey:key];
    }

    resolve(result);
  }];
}

@end
