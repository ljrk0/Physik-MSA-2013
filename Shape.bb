Type Shape
	Field PointAnz
	Field Point.Point[100]
End Type

Type Point
	Field Pos#[1]
End Type

Function Sh_AddPoint(Obj.Shape,X#,Y#)
	Obj\Point[Obj\PointAnz]=New Point
	Obj\Point[Obj\PointAnz]\Pos[0]=X
	Obj\Point[Obj\PointAnz]\Pos[1]=Y
	Obj\PointAnz=Obj\PointAnz+1
	
End Function

Function Sh_CreateSquare.Shape(X1#,Y1#,X2#,Y2#)
	Local CBox.Shape = New Shape
	Sh_AddPoint(CBox,X1,Y1)
	Sh_AddPoint(CBox,X2,Y1)
	Sh_AddPoint(CBox,X2,Y2)
	Sh_AddPoint(CBox,X1,Y2)
	Return CBox
End Function

Function Sh_GetShapeEge(Obj.Shape, minPoint#[1], maxPoint#[1])
	Local i
	
	minPoint[0]=10000
	minPoint[1]=10000
	maxPoint[0]=-10000
	maxPoint[1]=-10000
	
	For i = 0 To Obj\PointAnz-1
		If Obj\Point[i]\Pos[0] < minPoint[0] Then minPoint[0] = Obj\Point[i]\Pos[0]
		If Obj\Point[i]\Pos[0] > maxPoint[0] Then maxPoint[0] = Obj\Point[i]\Pos[0]
		If Obj\Point[i]\Pos[1] < minPoint[1] Then minPoint[1] = Obj\Point[i]\Pos[1]
		If Obj\Point[i]\Pos[1] > maxPoint[1] Then maxPoint[1] = Obj\Point[i]\Pos[1]
	Next
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D