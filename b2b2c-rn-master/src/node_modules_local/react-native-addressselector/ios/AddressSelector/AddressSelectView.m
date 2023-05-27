//
//  AddressSelectView.m
//  JavaMall
//
//  Created by LDD on 17/6/9.
//  Copyright © 2017年 Enation. All rights reserved.
//
#import "AddressSelectView.h"
#import "ESLabel.h"
#import "AddressSelectCell.h"
#import <Foundation/Foundation.h>
#import "UIColor+HexString.h"
#import <React/RCTRootView.h>
#import "PopUIController.h"
#import <React/RCTLog.h>
#import <objc/runtime.h>
@implementation AddressSelectView{
    UICollectionView *addressListView;
    NSMutableArray   *data;
    UIView           *background;
    ESLabel          *hint;
    NSNumber        *dataType;
    void (^selected)(Region *per,Region *city,Region *region,Region *town);
    void (^setData)(NSInteger rid,AddressSelectView *asv);
    UICollectionViewFlowLayout *listLayout;
    Region           *per;
    Region           *city;
    Region           *region;
    Region           *town;
    CGRect           kScreen_Bounds;
    CGFloat          kScreen_Height;
    CGFloat          kScreen_Width;
    UIWindow * keywindow ;
    PopUIController *uicontorll;
}

-(instancetype) initWithEvent:(void (^)(Region *per,Region *city,Region *region,Region *town))success  setDataSource:(void (^)(NSInteger rid,AddressSelectView *asv))setDataSource{
    selected = success;
    setData = setDataSource;
    self = [super init];
    if (self) {
        dispatch_sync(dispatch_get_main_queue(), ^{
            self.frame=CGRectMake(0,0, kScreen_Width, kScreen_Height);
            self.tag=1993;
            self.backgroundColor = [UIColor colorWithHexString:@"#22000000"];
            [self drawContent];
        });
    }
    return self;
}

+(instancetype) initWithEvent:(void (^)(Region *per,Region *city,Region *region,Region *town))success  setDataSource:(void (^)(NSInteger rid,AddressSelectView *asv))setDataSource{
    return [[self alloc]initWithEvent:success setDataSource:setDataSource];
}

-(void) drawContent{
    keywindow = [[UIApplication sharedApplication] keyWindow];
    kScreen_Width = [UIScreen mainScreen].bounds.size.width;
    kScreen_Bounds = [UIScreen mainScreen].bounds;
    kScreen_Height = [UIScreen mainScreen].bounds.size.height;
    hint=[[ESLabel alloc] initWithText:@"当前未选择" textColor:[UIColor colorWithHexString:@"#fff15353"] fontSize:14];
    background = [[UIView alloc]initWithFrame:CGRectMake(kScreen_Width*0.15,kScreen_Height*0.2, kScreen_Width*0.7, kScreen_Height*0.60)];
    background.backgroundColor = [UIColor whiteColor];
    background.layer.cornerRadius=5;
    background.tag = 1222;
    [self addSubview:background];
    UIView *titleBar = [[UIView alloc] initWithFrame:CGRectMake(0, 0, kScreen_Width*0.7, kScreen_Height*0.05)];
    titleBar.backgroundColor = [UIColor colorWithHexString:@"#fff15353"];
    [background addSubview:titleBar];
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:titleBar.bounds byRoundingCorners:UIRectCornerTopLeft | UIRectCornerTopRight cornerRadii:CGSizeMake(5,5)];
    CAShapeLayer *maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = titleBar.bounds;
    maskLayer.path = maskPath.CGPath;
    titleBar.layer.mask = maskLayer;
    [background addSubview:titleBar];
    ESLabel *title =[[ESLabel alloc] initWithText:@"地址" textColor:[UIColor whiteColor] fontSize:14];
    CGRect titleFrame = title.frame;
    title.textAlignment = NSTextAlignmentCenter;
    titleFrame.size = CGSizeMake(kScreen_Width*0.5, kScreen_Height*0.05);
    titleFrame.origin.y=0;
    titleFrame.origin.x = kScreen_Width*0.35-kScreen_Width*0.25;
    [title setFrame:titleFrame];
    [titleBar addSubview:title];
    ESLabel *canceltitle =[[ESLabel alloc] initWithText:@"返回" textColor:[UIColor whiteColor] fontSize:14];
    canceltitle.frame = CGRectMake(0, 0, kScreen_Width*0.15, kScreen_Height*0.05);
    canceltitle.textAlignment = NSTextAlignmentCenter;
    ESLabel *confirmtitle =[[ESLabel alloc] initWithText:@"确定" textColor:[UIColor whiteColor] fontSize:14];
    confirmtitle.frame = CGRectMake(0,0 , kScreen_Width*0.15, kScreen_Height*0.05);
    confirmtitle.textAlignment = NSTextAlignmentCenter;
    UIView *cancel = [[UIControl alloc]initWithFrame:CGRectMake(0, 0, kScreen_Width*0.15, kScreen_Height*0.05)];
    [(UIControl *)cancel addTarget:self action:@selector(back) forControlEvents:UIControlEventTouchUpInside];
    canceltitle.center = cancel.center;
    [cancel addSubview:canceltitle];
    cancel.backgroundColor=[UIColor colorWithHexString:@"#fff15353"];
    [background addSubview:cancel];
    UIBezierPath *cancelmaskPath = [UIBezierPath bezierPathWithRoundedRect:cancel.bounds byRoundingCorners:UIRectCornerTopLeft cornerRadii:CGSizeMake(5,5)];
    CAShapeLayer *cancelmaskLayer = [[CAShapeLayer alloc] init];
    cancelmaskLayer.frame = cancel.bounds;
    cancelmaskLayer.path = cancelmaskPath.CGPath;
    cancel.layer.mask = cancelmaskLayer;
    UIView *confrim = [[UIControl alloc]initWithFrame:CGRectMake(kScreen_Width*0.55,0 , kScreen_Width*0.15, kScreen_Height*0.05)];
    [(UIControl *)confrim addTarget:self action:@selector(confirm) forControlEvents:UIControlEventTouchUpInside];
    [confrim addSubview:confirmtitle];
    confrim.backgroundColor=[UIColor colorWithHexString:@"#fff15353"];
    [background addSubview:confrim];
    UIBezierPath *confrimmaskPath = [UIBezierPath bezierPathWithRoundedRect:confrim.bounds byRoundingCorners:UIRectCornerTopRight cornerRadii:CGSizeMake(5,5)];
    CAShapeLayer *confrimmaskLayer = [[CAShapeLayer alloc] init];
    confrimmaskLayer.frame = confrim.bounds;
    confrimmaskLayer.path = confrimmaskPath.CGPath;
    confrim.layer.mask = confrimmaskLayer;
    listLayout = [[UICollectionViewFlowLayout alloc] init];
    listLayout.minimumLineSpacing = 0;
    listLayout.minimumInteritemSpacing = 0;
    listLayout.itemSize = CGSizeMake(kScreen_Width*0.7, kScreen_Height*0.05);
    addressListView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, kScreen_Height*0.05, kScreen_Width*0.7, kScreen_Height*0.5-5) collectionViewLayout:listLayout];
    addressListView.backgroundColor = [UIColor colorWithHexString:@"#FAFAFA"];
    addressListView.delegate = self;
    addressListView.dataSource = self;
    [addressListView registerClass:[AddressSelectCell class] forCellWithReuseIdentifier:@"List"];
    [background addSubview:addressListView];
    UIView *hintParent = [[UIView alloc] initWithFrame:CGRectMake(0, (kScreen_Height*0.55), kScreen_Width*0.7, kScreen_Height*0.05)];
    hintParent.backgroundColor = [UIColor whiteColor];
    [hintParent addSubview:hint];
    hint.frame = CGRectMake(0, 0, kScreen_Width*0.7, kScreen_Height*0.05);
    hint.textAlignment = NSTextAlignmentCenter;
    [background addSubview:hintParent];
    UIBezierPath *hintmaskPath = [UIBezierPath bezierPathWithRoundedRect:hintParent.bounds byRoundingCorners:UIRectCornerBottomLeft|UIRectCornerBottomRight cornerRadii:CGSizeMake(5,5)];
    CAShapeLayer *hintmaskLayer = [[CAShapeLayer alloc] init];
    hintmaskLayer.frame = hintParent.bounds;
    hintmaskLayer.path = hintmaskPath.CGPath;
    hintParent.layer.mask = hintmaskLayer;
}

-(NSInteger) collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return data.count;
}

-(UICollectionViewCell *) collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
     AddressSelectCell *cell = [addressListView dequeueReusableCellWithReuseIdentifier:@"List" forIndexPath:indexPath];
     Region *region = [data objectAtIndex:[indexPath row]];
     [cell initData:region.name];
    return cell;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    Region *region1 = [data objectAtIndex:[indexPath row]];
    switch (region1.type.integerValue) {
        case 1:
            per = region1;
            break;
        case 2:
            city = region1;
            break;
        case 3:
            region = region1;
            break;
        case 4:
            town = region1;
            break;
        default:
             NSLog([NSString stringWithFormat:@"%d",region1.type]);
            break;
    }
    //RCTLog(region1.name);
    [self setHint];
    setData(region1.id.integerValue,self);
}
-(void) setHint{
     hint.text = [NSString stringWithFormat:@"%@ %@ %@ %@",per==nil?@"":per.name,city==nil?@"":city.name,region==nil?@"":region.name,town==nil?@"":town.name];
}
-(void) loadData:(NSMutableArray *)regions{
    if (regions==nil||regions.count==0) {
        return;
    }
    Region *region = [regions objectAtIndex:0];
    dataType = region.type;
    data=regions;
    dispatch_sync(dispatch_get_main_queue(), ^{
        [addressListView reloadData];
        [addressListView scrollsToTop];
    });
}

-(IBAction)back{
    //RCTLog([NSString stringWithFormat:@"%d",dataType.integerValue]);
    if (dataType.integerValue==1) {
        per = nil;
        city =nil;
        region=nil;
        town=nil;
        [self hideView];
    }else if(dataType.integerValue==2){
        city =nil;
        region=nil;
        town=nil;
        setData(0,self);
    }else if(dataType.integerValue==3){
        region=nil;
         town=nil;
        setData(per.id.integerValue,self);
    }else if(dataType.integerValue==4){
        town=nil;
        setData(city.id.integerValue,self);
    }
     [self setHint];
}

-(IBAction)confirm{
    if (region==nil) {
        RCTLog(@"AddressSelectView:未选择地址，或地址少于三级！");
    }
    selected(per,city,region,town);
    [self hideView];
}

-(void) showView{
    dispatch_async(dispatch_get_main_queue(), ^{
        [self initData];
        hint.text = @"当前未选择";
        UIViewController *vc = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
        vc.definesPresentationContext = YES;
        
            uicontorll = [PopUIController new];
            uicontorll.view = self;
            uicontorll.modalPresentationStyle = UIModalPresentationOverCurrentContext;
        
        [vc presentViewController:uicontorll animated:nil completion:nil];
    });
}
    

-(void) hideView{
    dispatch_async(dispatch_get_main_queue(), ^{
        [UIView animateWithDuration:0.2
                         animations:^{
                             // 第一步： 以动画的形式将view慢慢放大至原始大小的1.2倍
                             background.transform =
                             CGAffineTransformScale(CGAffineTransformIdentity, 1.2, 1.2);
                         }
                         completion:^(BOOL finished) {
                             [UIView animateWithDuration:0.3
                                              animations:^{
                                                  // 第二步： 以动画的形式将view缩小至原来的1/1000分之1倍
                                                  background.transform = CGAffineTransformScale(
                                                                                                CGAffineTransformIdentity, 0.001, 0.001);
                                              }
                                              completion:^(BOOL finished) {
                                                  // 第三步： 移除
                                                  [uicontorll dismissViewControllerAnimated:NO completion:nil];
                                              }];
                         }];
    });
    
}

- (UIViewController*) getRootVC {
    UIViewController *root = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
    while (root.presentedViewController != nil) {
        root = root.presentedViewController;
    }
    
    return root;
}

-(void) initData{
    dataType=[NSNumber numberWithInteger:1];
    setData(0,self);
    per=nil;
    city=nil;
    region = nil;
    town = nil;
}

+ (id)getObjectInternal:(id)obj{
    if([obj isKindOfClass:[NSString class]]
       || [obj isKindOfClass:[NSNumber class]]
       || [obj isKindOfClass:[NSNull class]])
    {
        return obj;
    }
    
    if([obj isKindOfClass:[NSArray class]])
    {
        NSArray *objarr = obj;
        NSMutableArray *arr = [NSMutableArray arrayWithCapacity:objarr.count];
        for(int i = 0;i < objarr.count; i++)
        {
            [arr setObject:[self getObjectInternal:[objarr objectAtIndex:i]] atIndexedSubscript:i];
        }
        return arr;
    }
    
    if([obj isKindOfClass:[NSDictionary class]])
    {
        NSDictionary *objdic = obj;
        NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithCapacity:[objdic count]];
        for(NSString *key in objdic.allKeys)
        {
            [dic setObject:[self getObjectInternal:[objdic objectForKey:key]] forKey:key];
        }
        return dic;
    }
    return [self getObjectData:obj];
}

+ (NSDictionary*)getObjectData:(id)obj
{
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    unsigned int propsCount;
    objc_property_t *props = class_copyPropertyList([obj class], &propsCount);//获得属性列表
    for(int i = 0;i < propsCount; i++)
    {
        objc_property_t prop = props[i];
        
        NSString *propName = [NSString stringWithUTF8String:property_getName(prop)];//获得属性的名称
        id value = [obj valueForKey:propName];//kvc读值
        if(value == nil)
        {
            value = [NSNull null];
        }
        else
        {
            value = [self getObjectInternal:value];//自定义处理数组，字典，其他类
        }
        [dic setObject:value forKey:propName];
    }
    return dic;
}


@end
