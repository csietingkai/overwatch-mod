path='E:\[3-2]Project\document\';
cd(path);

%mod = imread(strcat(path, 'Mod.png'));
%imshow(mod);
mod_c = imread(strcat(path, 'Mod_c.png'));  % 1134*162
%imshow(mod_c);

%mod1 = imcrop(mod, [0, 50, 1008, 216]);
%imshow(mod1);
%imwrite(mod1, strcat(path, 'Mod-1.png'));
%mod2 = imcrop(mod, [1008, 50, 1008, 216]);
%imshow(mod2);
%imwrite(mod2, strcat(path, 'Mod-2.png'));

mod1 = imcrop(mod_c, [0, 0, 452, 162]);
imshow(mod1);
imwrite(mod1, strcat(path, 'Mod-1.png'));
mod2 = imcrop(mod_c, [460, 0, 674, 162]);
imshow(mod2);
imwrite(mod2, strcat(path, 'Mod-2.png'));
clear path;
clear mod;
clear mod_c;
clear mod1;
clear mod2;

