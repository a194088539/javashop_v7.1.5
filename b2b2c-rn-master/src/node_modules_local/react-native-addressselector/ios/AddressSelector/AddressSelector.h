//
//  AddressSelector.h
//  AddressSelector
//
//  Created by LDD on 2017/11/1.
//  Copyright © 2017年 LDD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <UIKit/UIKit.h>
#import "AddressSelectView.h"
#import <React/RCTEventEmitter.h>
@interface AddressSelector : RCTEventEmitter<RCTBridgeModule>

@property(strong,nonatomic) AddressSelectView *addressView;

@end
