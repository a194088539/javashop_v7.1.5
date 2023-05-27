//
//  PopUIController.m
//  AddressSelector
//
//  Created by LDD on 2017/11/2.
//  Copyright © 2017年 LDD. All rights reserved.
//

#import "PopUIController.h"

@interface PopUIController ()

@end

@implementation PopUIController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.view viewWithTag:1222].transform = CGAffineTransformScale(CGAffineTransformIdentity,
                                                 0.01, 0.01);
}

- (void)viewDidAppear:(BOOL)animated{
    // 第一步：将view宽高缩至无限小（点）
    [self.view viewWithTag:1222].transform = CGAffineTransformScale(CGAffineTransformIdentity,
                                                 0.01, 0.01);
    [UIView animateWithDuration:0.3
                     animations:^{
                         // 第二步： 以动画的形式将view慢慢放大至原始大小的1.2倍
                         [self.view viewWithTag:1222].transform =
                         CGAffineTransformScale(CGAffineTransformIdentity, 1.2, 1.2);
                     }
                     completion:^(BOOL finished) {
                         [UIView animateWithDuration:0.2
                                          animations:^{
                                              // 第三步： 以动画的形式将view恢复至原始大小
                                              [self.view viewWithTag:1222].transform = CGAffineTransformIdentity;
                                          }];
                     }];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
