Include "Help.bb"
Include "Ph_Object.bb"
Include "Ph_Collision.bb"

;-----------------------------------------------------
; MAINPHYSICTICK
; Calls the DoTick-Funciton for each Object
;-----------------------------------------------------

Function MainPhysicTick(t#)
	Local obj.Ph_Object
	Ph_DoCollision(t,0.000000001)
	For obj = Each Ph_Object
		Ph_DoTick(obj, t)
	Next
End Function

;------------------------------------------------------
; MAINPHYSICSRENDER
; Calls the Render-Function for each Object
;------------------------------------------------------

Function MainPhysicRender()
	Local obj.Ph_Object
	For obj = Each Ph_Object
		If obj\CollisionBox <> Null Then Ph_Render(obj)
	Next
	Local o.Ph_Object = First Ph_Object
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D