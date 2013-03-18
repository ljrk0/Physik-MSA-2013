Function Ph_DoCollision(t#, maxabs#)
	Local obj.Ph_Object
	Local obj2.Ph_Object
	Local virtual.Ph_Object
	Local Temp#[1]
	Local a#[0]
	Local Temp1#[1]
	Local Temp2#[1]
	obj = First Ph_Object
	Repeat
		obj2 = After obj
		Repeat
			If Ph_CollideObjectAfterTime(obj, obj2, t, Temp,a) Then
				Local i=0
				Local t2#=0.5
				Repeat
					i=i+1
					If Ph_CollideObjectAfterTime(obj, obj2, t*t2, Temp,a) Then
						t2=t2-(1/(2^i))
					Else
						t2=t2-(1/(2^i))
					EndIf
				Until (t2*t)*VectorLenght(obj\Vel)<maxabs And (t2*t)*VectorLenght(obj2\Vel)<maxabs
				
				If Ph_CollideObjectAfterTime(obj, obj2, t*t2, Temp,a) Then
					t2=t2-(1/(2^i))
				EndIf
				Ph_CollideObjectAfterTime(obj, obj2, t*(t2+(1/(2^i))),Temp,a)
				
				Ph_RelativatePosition(obj,Temp,Temp1)
				Ph_RelativatePosition(obj2,Temp,Temp2)
				
				Ph_ApplyCollision(obj,obj2, Temp1, Temp2, a[0])
				
			EndIf
			If obj2 = Last Ph_Object Then
				Exit
			Else
				obj2=After obj2
			EndIf
		Forever
		obj = After obj
		If obj = Last Ph_Object Then Exit
	Forever
	
End Function

Function Ph_CollideObjectAfterTime(obj1.Ph_Object, obj2.Ph_Object, t#, P#[1], angle#[0])
	Local virtual1.Ph_Object = Ph_GetVirtualCopyAfterTime(obj1,t)
	Local virtual2.Ph_Object = Ph_GetVirtualCopyAfterTime(obj2,t)
	
	Local virtualShape1.Shape = Ph_GetAbsolutCollisionBox(virtual1)
	Local virtualShape2.Shape = Ph_GetAbsolutCollisionBox(virtual2)
	
	Local rBank = Ph_CollisionBoxColliding(virtualShape1,virtualShape2)
	
	Delete virtual1
	Delete virtual2
	Delete virtualShape1
	Delete virtualShape2
	
	Return rBank
End Function

Function Ph_ApplyCollision(obj1.Ph_Object,obj2.Ph_Object, pos_obj1#[1], pos_obj2#[1], vector#)
	; TODO Apply the Collision Forces / Stoesse
	Return FalseEnd Function
;~IDEal Editor Parameters:
;~C#Blitz3D