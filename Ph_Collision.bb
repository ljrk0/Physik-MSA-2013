Function Ph_DoCollision(t#, maxabs#, k#)
	Local obj.Ph_Object
	Local obj2.Ph_Object
	Local virtual.Ph_Object
	Local Temp#[1]
	Local Temp1#[1]
	Local Temp2#[1]
	Local tBank
	obj = First Ph_Object
	If obj=Null Then Return
	If obj\CollisionBox=Null Then Return
	Repeat
		obj2 = After obj
		If obj2=Null Then Return
		If obj2\CollisionBox=Null Then Return
		Repeat
			If Ph_CollideObjectAfterTime(obj, obj2, t) Then
				Local i=1
				Local t2#=0.5
				Local lt#=1
				Repeat
					i=i+1
					If Ph_CollideObjectAfterTime(obj, obj2, t*t2) Then
						lt=t2
						t2=t2-(1/(2^i))
					Else
						t2=t2-(1/(2^i))
					EndIf
				Until (t2*t)*VectorLenght(obj\Vel)<maxabs And (t2*t)*VectorLenght(obj2\Vel)<maxabs
				
				If Ph_CollideObjectAfterTime(obj, obj2, t*t2) Then
					lt=t2
					t2=t2-(1/(2^i))
				EndIf
				tBank = Ph_CollideObjectAfterTime(obj, obj2, t*lt)
				Temp[0] = PeekFloat(tBank,0)
				Temp[1] = PeekFloat(tBank,4)
				
				
				
				Ph_RelativatePosition(obj,Temp,Temp1)
				Ph_RelativatePosition(obj2,Temp,Temp2)
				
				Ph_ApplyCollision(obj,obj2, Temp1, Temp2, PeekFloat(tBank,8), k)
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

Function Ph_CollideObjectAfterTime(obj1.Ph_Object, obj2.Ph_Object, t#)
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

Function Ph_ApplyCollision(obj1.Ph_Object,obj2.Ph_Object, pos_obj1#[1], pos_obj2#[1], angle#, k#)
	; TODO Apply the Collision Forces / Stoesse
	
	Local PVel1#[1], PVel2#[1]
	
	RotateVector(pos_obj1,obj1\RotVel,PVel1)
	SubtractVector(pos_obj1,PVel1, PVel1)
	AddVector(PVel1, obj1\Vel, PVel1)
	
	RotateVector(pos_obj2,obj2\RotVel,PVel2)
	SubtractVector(pos_obj2, PVel2, PVel2)
	AddVector(PVel2, obj2\Vel, PVel2)
	
	If obj1\Fixed And obj2\Fixed Then Return
	
	Local obj1vel1norm#[1]
	Local obj2vel1norm#[1]
	Local obj1vel1Lenght#
	Local obj2vel1Lenght#
	Local obj1vel2norm#[1]
	Local obj2vel2norm#[1]
	Local obj1vel2Lenght#
	Local obj2vel2Lenght#
	Local obj1vel2LenghtC#
	Local obj2vel2LenghtC#
	Local obj1vel#[1]
	Local obj2vel#[1]
	
	; Berechnug der unbeeinflussten Teile (vel1)
	
	obj1vel1norm[0]=1
	obj1vel1norm[1]=0	
	RotateVector(obj1vel1norm,angle,obj1vel1norm)
	
	obj2vel1norm[0]=1
	obj2vel1norm[1]=0	
	RotateVector(obj2vel1norm,angle + Pi,obj2vel1norm)
	
	Local temp#[1]
	temp[0]=1
	temp[1]=0	
	
	If VectorLenght(PVel1) = 0 Then obj1vel1Lenght = 0 Else obj1vel1Lenght=Sin(90-RadToDeg(VectorAngle(PVel1,temp)-angle))*VectorLenght(PVel1)
	If VectorLenght(PVel2) = 0 Then obj2vel1Lenght = 0 Else obj2vel1Lenght=Sin(90-RadToDeg(VectorAngle(PVel2,temp)-angle))*VectorLenght(PVel2)
	
	; Berechung der beeinflussten Teile der Geschwindigkeit (vel2)
	
	obj1vel2norm[0]=1
	obj1vel2norm[1]=0	
	RotateVector(obj1vel2norm,angle-(Pi/2),obj1vel2norm)
	
	obj2vel2norm[0]=1
	obj2vel2norm[1]=0	
	RotateVector(obj2vel2norm,angle+(Pi/2),obj2vel2norm)
	
	If VectorLenght(PVel1) = 0 Then obj1vel2Lenght = 0 Else obj1vel2Lenght=Sin(RadToDeg(VectorAngle(PVel1,temp)-angle))*VectorLenght(PVel1)
	If VectorLenght(PVel2) = 0 Then obj2vel2Lenght = 0 Else obj2vel2Lenght=Sin(RadToDeg(VectorAngle(PVel2,temp)-angle))*VectorLenght(PVel2)
	
	
	; berechnung der Stosses
	
	obj1vel2LenghtC = (obj1\Mass*obj1vel2Lenght + obj2\Mass*obj2vel2Lenght - obj2\Mass*(obj1vel2Lenght-obj2vel2Lenght)*k)/(obj1\Mass + obj2\Mass)
	obj2vel2LenghtC = (obj1\Mass*obj1vel2Lenght + obj2\Mass*obj2vel2Lenght - obj1\Mass*(obj2vel2Lenght-obj1vel2Lenght)*k)/(obj1\Mass + obj2\Mass)
	; berechnug der Endgeschwindigkeit (Vectoraddition)
	
	MultiplyVector(obj1vel1norm,obj1vel1Lenght,obj1vel1norm)
	MultiplyVector(obj1vel2norm,obj1vel2LenghtC,obj1vel2norm)
	MultiplyVector(obj2vel1norm,obj2vel1Lenght,obj2vel1norm)
	MultiplyVector(obj2vel2norm,obj2vel2LenghtC,obj2vel2norm)
	
	AddVector(obj1vel1norm, obj1vel2norm, obj1vel)
	AddVector(obj2vel1norm, obj2vel2norm, obj2vel)
	
	;berechnung der anteile fuer Bewegung und Rotation
	; - obj1
	
	;If obj1\Fixed Then
	;	SubtractVector(obj1vel, obj2vel, obj1vel)
	;Else If obj2\Fixed Then
	;	SubtractVector(obj2vel, obj1vel, obj2vel)
	;EndIf
	
	angle = VectorAngle(pos_obj1, obj1vel)
	obj1\Vel[0]=1
	obj1\Vel[1]=0
	RotateVector(obj1\Vel, (Pi/2) - angle, obj1\Vel)
	MultiplyVector(obj1\Vel, Sin((90-RadToDeg(angle))*VectorLenght(obj1vel)), obj1\Vel)
	
	obj1\RotVel=obj1\RotVel+((Sin(RadToDeg(angle))*VectorLenght(obj1vel))/VectorLenght(pos_obj1))
	
	obj1\PreventFromTick = True
	; - obj2
	
	angle = VectorAngle( obj2vel, pos_obj2 )
	obj2\Vel[0]=1
	obj2\Vel[1]=0
	RotateVector(obj2\Vel, (Pi/2) - angle, obj2\Vel)
	MultiplyVector(obj2\Vel, Sin((90-RadToDeg(angle))*VectorLenght(obj2vel)), obj2\Vel)
	
	obj2\RotVel=obj2\RotVel+((Sin(RadToDeg(angle))*VectorLenght(obj2vel))/VectorLenght(pos_obj2))
	
	obj2\PreventFromTick = True
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D