//
// Created by Dawei on 4/30/16.
// Copyright (c) 2016 Enation. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface ESLabel : UILabel

- (instancetype) initWithFont:(UIFont *)font textColor:(UIColor *)textColor;

- (instancetype) initWithText:(NSString *)text textColor:(UIColor *)textColor fontSize:(NSInteger)fontSize;

@end
