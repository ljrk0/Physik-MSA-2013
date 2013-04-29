Include "Ph_CollisionBox.bb"


Type Ph_Object
	Field Image
	Field Pos#[1]   ; m
	Field Vel#[1]   ; m/s
	Field Acc#[1]   ; m/s²
	Field Rot#      ; rad
	Field RotVel#   ; rad/s
	Field RotAcc#   ; rad/s²
	Field Mass#     ; kg
	Field RotMass#  ; kg/px²
	Field CollisionBox.Shape
	Field Virtual   ; true/false
	Field Fixed		; true/false
	Field friction_value# ; scalar        
	Field FixedPos#[1], FixedRot#
	Field maxTick# ;true/false
End Type

Type Ph_Force
	Field Obj.Ph_Object
	Field Force#[1], approach#[1], Relative
	Field time
End Type

;------------------------------------------------------------
; PH_DOTICK
; Calculates basic tick-related things eg. acceleration,         
;-------------------------------------------------------------


Function Ph_DoTick(Obj.Ph_Object, Time#)
	If Obj\maxTick < Time And Obj\maxTick > 0 Then Time = Obj\maxTick
	Obj\maxTick=0
	
	If Obj\Fixed Then
		Obj\Vel[0] = 0
		Obj\Vel[1] = 0
		Obj\RotVel = 0
		; reset Acceleration
		Obj\Acc[0] = 0
		Obj\Acc[1] = 0
		Obj\RotAcc = 0
		Return
	EndIf
	
	Local a#[1]
	; Apply acceleration to velocity 
	
	MultiplyVector(Obj\Acc, Time, a)
	
	AddVector(Obj\Vel,a,Obj\Vel)
	Obj\RotVel=Obj\RotVel+Obj\RotAcc*Time
	
	; Apply velocity to position
	
	MultiplyVector(Obj\Vel, Time, a)
	
	AddVector(Obj\Pos,a,Obj\Pos)
	Obj\Rot=Obj\Rot+Obj\RotVel*Time
	; reset Acceleration
	Obj\Acc[0] = 0
	Obj\Acc[1] = 0
	Obj\RotAcc = 0
End Function

;----------------------------------------------------------
;PH_APPLYFORCE
;Applys a force at a point of an object - use this to
; apply forces
;----------------------------------------------------------

Function Ph_ApplyForce(Obj.Ph_Object, Force#[1], approach#[1], Relative = True)
	Local Force2#[1]
	Local approach2#[1]
	If Relative Then
		RotateVector(Force,Obj\Rot,Force2)
		RotateVector(approach,Obj\Rot,approach2)
	Else
		Force2[0]=Force[0]
		Force2[1]=Force[1]
		approach2[0]=approach[0]
		approach2[1]=approach[1]
	EndIf
	
	If VectorLength(Force2)=0 Then Return
	If VectorLength(approach2)=0 Then
		Ph_ApplyVelForce(Obj,Force2)
	Else
		
		
			
		Local a#[1]
		MultiplyVector(approach2,-1,a)
		Local Angle# = RadToDeg(VectorAngle(a,Force2))
		Ph_ApplyRotTorque(Obj,Sin(Angle)*VectorLength(Force2)*VectorLength(approach2))
		Local b#[1]
		NormalizeVector(a,b)
		
		MultiplyVector(b, Sin(90-Angle)*VectorLength(Force2),b)
		
		Ph_ApplyVelForce(Obj,b)
	EndIf
	
End Function

;-------------------------------------------------------
;PH_APPLYVELFORCE
;Applys a force, that directly causes an acceleration
;-------------------------------------------------------

Function Ph_ApplyVelForce(Obj.Ph_Object, Force#[1])
	Local a#[1]
	DivideVector(Force,Obj\Mass, a)
	AddVector(Obj\Acc,a,Obj\Acc)
End Function

;-------------------------------------------------------
; PH_APPLYROTTORQUE
; Applys a torque to an object
;-------------------------------------------------------

Function Ph_ApplyRotTorque(Obj.Ph_Object, Torque#)
	Obj\RotAcc=Obj\RotAcc+Torque/Obj\RotMass
End Function

;-------------------------------------------------------
; PH_RENDER
; Draws the object on-screen
;-------------------------------------------------------

Function Ph_Render(Obj.Ph_Object)   ; 1 m ^= 100px
	Ph_DrawImagefromCollisonBox(Ph_GetAbsolutCollisionBox(Obj))
	Local temp#[1]
	temp[0]=20
	temp[1]=0
	Color 100,100,255
	Plot Obj\Pos[0]*100,Obj\Pos[1]*100
	Color 255,255,0
	RotateVector(temp, Obj\Rot, temp)
	Plot Obj\Pos[0]*100 + temp[0],Obj\Pos[1]*100 + temp[1]
End Function

;----------------------------------------------------------
; PH_GETVIRTUALCOPYAFTERTIME
; Copys an object needed for collision-checking
;----------------------------------------------------------

Function Ph_GetVirtualCopyAfterTime.Ph_Object(obj.Ph_Object,t#)
	Local result.Ph_Object = New Ph_Object
	result\Acc[0] = obj\Acc[0]
	result\Acc[1] = obj\Acc[1]
	result\CollisionBox = obj\CollisionBox
	result\Mass = obj\Mass
	result\Pos[0] = obj\Pos[0]
	result\Pos[1] = obj\Pos[1]
	result\Rot = obj\Rot
	result\RotAcc = obj\RotAcc
	result\RotMass = obj\RotMass
	result\RotVel = obj\RotVel
	result\Vel[0] = obj\Vel[0]
	result\Vel[1] = obj\Vel[1]
	result\Virtual = True
	
	Ph_DoTick(result, t)
	Return result
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D