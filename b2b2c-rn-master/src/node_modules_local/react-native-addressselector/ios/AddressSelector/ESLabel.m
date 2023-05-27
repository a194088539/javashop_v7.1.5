//
// Created by Dawei on 4/30/16.
// Copyright (c) 2016 Enation. All rights reserved.
//

#import "ESLabel.h"

@implementation ESLabel {

}

- (instancetype)initWithFont:(UIFont *)font textColor:(UIColor *)textColor {
    self = [super init];
    if (self) {
        self.font = font;
        self.textColor = textColor;
    }
    return self;
}

- (instancetype) initWithText:(NSString *)text textColor:(UIColor *)textColor fontSize:(NSInteger)fontSize{
    self = [super init];
    if (self) {
        self.font = [UIFont systemFontOfSize:fontSize];
        self.textColor = textColor;
        self.text = text;
    }
    return self;
}

@end