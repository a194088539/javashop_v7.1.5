//
//  AddressSelectCell.h
//  JavaMall
//
//  Created by LDD on 17/6/9.
//  Copyright © 2017年 Enation. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ESLabel.h"

@interface AddressSelectCell : UICollectionViewCell

@property (strong, nonatomic) ESLabel *nameLabel;

-(void) initData:(NSString *)addressTitle;
@end
