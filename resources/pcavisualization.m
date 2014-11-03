clc
%import data into cho_var
data = importdata('out.txt');
[rows, columns] = size(data);
features = data(1:rows,3:columns);
label = data(1:rows, 2:2);
coeff = princomp(features);
multiplier = features-mean2(features);
mxc = multiplier*coeff(:,1:2);
%plot(mxc(:,1),mxc(:,2),'.');
scatter(mxc(:,1),mxc(:,2),3,label);


