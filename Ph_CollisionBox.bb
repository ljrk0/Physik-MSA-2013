Include "Shape.bb"

;---------------------------------------------------
;PH_CALCULATEMOMENTOFINERTIA
; Calculates the moment of inertia from a mass and
; a Collision-box/Shape
;---------------------------------------------------
Function Ph_CalculateMomentOfInertia#(Obj.Shape, mass#)
	Local Sum1#, Sum2#
	Local i
	For i=0 To Obj\PointCount-1
		Sum1=Sum1+Abs(VectorCrossProduct(Obj\Point[(i+1) Mod Obj\PointCount]\Pos,Obj\Point[i]\Pos))*(VectorScalarProduct(Obj\Point[(i+1) Mod Obj\PointCount]\Pos,Obj\Point[(i+1) Mod Obj\PointCount]\Pos) + VectorScalarProduct(Obj\Point[(i+1) Mod Obj\PointCount]\Pos,Obj\Point[i]\Pos) + VectorScalarProduct(Obj\Point[i]\Pos,Obj\Point[i]\Pos))
		Sum2=Sum2+Abs(VectorCrossProduct(Obj\Point[(i+1) Mod Obj\PointCount]\Pos,Obj\Point[i]\Pos))
	Next
	Return ((mass/6)*(Sum1/Sum2))
End Function

;-----------------------------------------------------
;PH_GETABSOLUTCOLLISIONBOX
; Transfers an CollisonBox to the gloal          
; coordinate system
;-----------------------------------------------------
Function Ph_GetAbsolutCollisionBox.Shape(obj.Ph_Object)
	Local obj2.Shape = obj\CollisionBox ; nur zur vereinfachten Schreibweise
	Local result.Shape = New Shape
	result\PointCount = obj2\PointCount
	Local i
	For i = 0 To obj2\PointCount-1
		result\Point[i] = New Point
		RotateVector(obj2\Point[i]\Pos, obj\Rot, result\Point[i]\Pos)
		AddVector(result\Point[i]\Pos,obj\Pos,result\Point[i]\Pos)
	Next
	Return result
End Function

;-------------------------------------------------------
;PH_RELATIVEPOSITION
; Transfers a global coordinate to the objects
; coordinate system
;-------------------------------------------------------

Function Ph_RelativatePosition(obj.Ph_Object, P1#[1], P2#[1])
	SubtractVector(P1,obj\Pos,P2)
	RotateVector(P2,-obj\Rot,P2)
End Function

;------------------------------------------------------
;PH_COLLISIONBOXCOLLIDING
; Checks, wether two (absolute) collisionboxes are
; collidiong or not
;------------------------------------------------------

Function Ph_CollisionBoxColliding(obj1.Shape,obj2.Shape)
	Local i,j
	Local count = 0
	Local temp#[1]
	Local temp2#[1]
	Local Pos0#[1]
	Local Pos1#[1]
	Cls
	For i=1 To obj1\PointCount
		For j=1 To obj2\PointCount
			If LineLine(obj1\Point[i Mod obj1\PointCount]\Pos, obj1\Point[i-1]\Pos, obj2\Point[j Mod obj2\PointCount]\Pos, obj2\Point[j-1]\Pos, temp) Then
				If count=0 Then
					Pos0[0] = temp[0]
					Pos0[1] = temp[1]
				Else
					Pos1[0] = temp[0]
					Pos1[1] = temp[1]
				EndIf
				
				count=count+1
			EndIf
		Next
	Next
	Local Bank
	If count=>2 Then
		Bank = CreateBank(12)
		
		
		
		
		AddVector(Pos0,Pos1,temp)
		DivideVector(temp,2,temp)
		PokeFloat Bank, 0, temp[0]
		PokeFloat Bank, 4, temp[1]
		
		temp2[0]=1
		temp2[1]=0
		
		
		
		SubtractVector(Pos1,Pos0,temp)
		If VectorLength(temp)=0 Then Return 0
		PokeFloat Bank, 8, VectorAngle(temp,temp2)
		
		Return Bank
;	Else If count=1
;		Bank = CreateBank(12)
		
;		PokeFloat Bank, 0, Pos0[0]
;		PokeFloat Bank, 4, Pos0[1]
		
;		temp2[0]=1
;		temp2[1]=0
		
;		SubtractVector(Pos1,Pos0,temp)
;		If VectorLength(temp)=0 Then Return 0
;		PokeFloat Bank, 8, VectorAngle(temp,temp2)
		
;		Return Bank
	Else
		Return 0
	EndIf
End Function

;----------------------------------------------
;PH_DRAWIMAGEFROMCOLLISIONBOX
; Draws an collision-Box on-screen
;----------------------------------------------
Function Ph_DrawImagefromCollisonBox(Obj.Shape)
	Local i
	Local r=ColorRed()
	Local g=ColorGreen()
	Local b=ColorBlue()
	Color 255,0,0
	Line Obj\Point[Obj\PointCount-1]\Pos[0]*100,Obj\Point[Obj\PointCount-1]\Pos[1]*100,Obj\Point[i]\Pos[0]*100,Obj\Point[i]\Pos[1]*100
	For i = 1 To Obj\PointCount-1
		Line Obj\Point[i-1]\Pos[0]*100,Obj\Point[i-1]\Pos[1]*100,Obj\Point[i]\Pos[0]*100,Obj\Point[i]\Pos[1]*100
		;DebugLog Obj\Point[i-1]\Pos[0]*100
	Next
	SetBuffer BackBuffer()
	Color r,g,b
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D