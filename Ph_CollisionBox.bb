Include "Shape.bb"

Function Ph_CalculateMomentOfInertia#(Obj.Shape, mass#)
	Local Sum1#, Sum2#
	Local i
	For i=0 To Obj\PointAnz-1
		Sum1=Sum1+Abs(VectorCrossProduct(Obj\Point[(i+1) Mod Obj\PointAnz]\Pos,Obj\Point[i]\Pos))*(VectorScalarProduct(Obj\Point[(i+1) Mod Obj\PointAnz]\Pos,Obj\Point[(i+1) Mod Obj\PointAnz]\Pos) + VectorScalarProduct(Obj\Point[(i+1) Mod Obj\PointAnz]\Pos,Obj\Point[i]\Pos) + VectorScalarProduct(Obj\Point[i]\Pos,Obj\Point[i]\Pos))
		Sum2=Sum2+Abs(VectorCrossProduct(Obj\Point[(i+1) Mod Obj\PointAnz]\Pos,Obj\Point[i]\Pos))
	Next
	Print Sum1
	Print Sum2
	Return ((mass/6)*(Sum1/Sum2))
End Function

Function Ph_GetAbsolutCollisionBox.Shape(obj.Ph_Object)
	Local obj2.Shape = obj\CollisionBox ; nur zur vereinfachten Schreibweise
	Local result.Shape = New Shape
	result\PointAnz = obj2\PointAnz
	Local i
	For i = 0 To obj2\PointAnz-1
		result\Point[i] = New Point
		RotateVector(obj2\Point[i]\Pos, obj\Rot, result\Point[i]\Pos)
	Next
	Return result
End Function

Function Ph_RelativatePosition(obj.Ph_Object, P1#[1], P2#[1])
	AddVector(P1,obj\Pos,P2)
	RotateVector(P2,obj\Rot,P2)
End Function

Function Ph_CollisionBoxColliding(obj1.Shape,obj2.Shape)
	Local i,j
	Local count = 0
	Local temp#[1]
	Local temp2#[1]
	Local Pos0#[1]
	Local Pos1#[1]
	For i=1 To obj1\PointAnz
		For j=1 To obj2\PointAnz
			If IsIntersecting(obj1\Point[i Mod obj1\PointAnz]\Pos, obj1\Point[i-1]\Pos,obj2\Point[j Mod obj2\PointAnz]\Pos, obj2\Point[i-1]\Pos, temp) Then
				If count=0 Then
					Pos0[0] = temp[0]
					Pos0[1] = temp[1]
				Else
					Pos1[0] = temp[0]
					Pos1[1] = temp[1]
				EndIf
				count=count+1
			EndIf
			If count=>2 Then Exit
		Next
		If count=>2 Then Exit
	Next
	If count=2 Then
		Local Bank = CreateBank(12)
		
		AddVector(Pos0,Pos1,temp)
		DivideVector(temp,2,temp)
		PokeFloat Bank, 0, temp[0]
		PokeFloat Bank, 4, temp[1]
		
		temp2[0]=1
		temp2[1]=0
		SubtractVector(Pos1,Pos0,temp)
		PokeFloat Bank, 8, VectorAngle(temp,temp2)
		Return Bank
	Else 
		Return 0
	EndIf
End Function

Function Ph_CreateImagefromCollisonBox(Obj.Shape)
	Local minPoint#[1]
	Local maxPoint#[1]
	Sh_GetShapeEge(Obj,minPoint,maxPoint)
	MultiplyVector(minPoint,100,minPoint)
	MultiplyVector(maxPoint,100,maxPoint)
	Local a#[1]
	SubtractVector(maxPoint,minPoint,a)
	Local Image = CreateImage(a[0]+1,a[1]+1)
	MultiplyVector(minPoint,-1,a)
	HandleImage Image,a[0],a[1]
	
	Local i
	SetBuffer ImageBuffer(Image)
	Local r=ColorRed()
	Local g=ColorGreen()
	Local b=ColorBlue()
	Color 255,0,0
	Line Obj\Point[Obj\PointAnz-1]\Pos[0]*100+a[0],Obj\Point[Obj\PointAnz-1]\Pos[1]*100+a[1],Obj\Point[i]\Pos[0]*100+a[0],Obj\Point[i]\Pos[1]*100+a[1]
	For i = 1 To Obj\PointAnz-1
		Line Obj\Point[i-1]\Pos[0]*100+a[0],Obj\Point[i-1]\Pos[1]*100+a[1],Obj\Point[i]\Pos[0]*100+a[0],Obj\Point[i]\Pos[1]*100+a[1]
	Next
	SetBuffer BackBuffer()
	Color r,g,b
	Return Image
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D