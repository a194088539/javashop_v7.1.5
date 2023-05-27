//
//  AddressSelectCell.m
//  JavaMall
//
//  Created by LDD on 17/6/9.
//  Copyright © 2017年 Enation. All rights reserved.
//

#import "AddressSelectCell.h"
@implementation AddressSelectCell

@synthesize nameLabel;

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
           [self createUI];
      }
    return self;
}

-(void) createUI{
    self.backgroundColor = [UIColor whiteColor];
    CGFloat kScreen_Width = [UIScreen mainScreen].bounds.size.width;
    CGFloat kScreen_Height = [UIScreen mainScreen].bounds.size.height;
    nameLabel = [[ESLabel alloc] initWithText:@"" textColor:[UIColor blackColor] fontSize:14];
    nameLabel.frame = CGRectMake(0, 0, kScreen_Width*0.7, kScreen_Height*0.05);
    nameLabel.textAlignment = NSTextAlignmentCenter;
    [self.contentView addSubview:nameLabel];
}

-(void) initData:(NSString *)addressTitle{
       self.nameLabel.text = addressTitle;
}

@end
