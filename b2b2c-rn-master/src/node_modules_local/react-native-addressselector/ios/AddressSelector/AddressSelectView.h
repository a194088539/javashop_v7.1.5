//
//  AddressSelectView.h
//  JavaMall
//
//  Created by LDD on 17/6/9.
//  Copyright © 2017年 Enation. All rights reserved.
//
#import "Region.h"
#import <UIKit/UIKit.h>
#import <React/RCTBridgeModule.h>
@interface AddressSelectView : UIView<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout>

-(instancetype) initWithEvent:(void (^)(Region *per,Region *city,Region *region,Region *town))success  setDataSource:(void (^)(NSInteger rid,AddressSelectView *asv))setDataSource;

+(instancetype) initWithEvent:(void (^)(Region *per,Region *city,Region *region,Region *town))success  setDataSource:(void (^)(NSInteger rid,AddressSelectView *asv))setDataSource;


-(void) loadData:(NSMutableArray *)regions;
 

/**
 * 移除View
 */
-(void) hideView;

/**
 * 显示View
 */
-(void) showView;

+ (NSDictionary*)getObjectData:(id)obj;
@end
